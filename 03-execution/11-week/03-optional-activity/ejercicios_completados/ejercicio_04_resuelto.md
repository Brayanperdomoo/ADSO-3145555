## Ejercicio 04 - Gestión de millas y evolución de niveles en el programa de fidelización
Modelo de datos del sistema
## 1. Descripción general

El modelo corresponde a una plataforma de datos para una aerolínea, construida bajo un enfoque relacional que permite gestionar los procesos esenciales del negocio. Dentro de estos procesos se incluyen: información geográfica, identificación de personas, seguridad, clientes, programas de fidelización, aeropuertos, aeronaves, operación de vuelos, reservas, emisión de tiquetes, abordaje, pagos y facturación.

El modelo está normalizado y organizado por módulos funcionales, donde las tablas se relacionan mediante claves foráneas que aseguran integridad, consistencia y trazabilidad en toda la operación.

## 2. Análisis del modelo

A partir del análisis realizado se concluye que:

el sistema contiene más de 60 entidades,
existe coherencia en la estructura relacional,
se aplican restricciones como PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
permite trazabilidad completa desde la actividad comercial hasta procesos posteriores como pagos o fidelización.

Esto confirma que se trata de un modelo de nivel empresarial.

## 3. Dominios funcionales
DATOS GENERALES

Tablas: time_zone, continent, country, city, currency
Función: Manejo de ubicaciones y monedas.

AEROLÍNEA

Tablas: airline
Función: Representación de la empresa.

IDENTIDAD

Tablas: person
Función: Identificación de usuarios.

SEGURIDAD

Tablas: user_account y roles
Función: Control de acceso.

CLIENTES Y FIDELIZACIÓN

Tablas: customer, loyalty_account, loyalty_program, loyalty_tier, loyalty_account_tier, miles_transaction
Función: Gestión de clientes, cuentas y niveles.

OPERACIÓN Y VENTAS

Tablas: reservation, sale
Función: Actividad comercial.

## 4. Enfoque del ejercicio

El ejercicio busca trabajar directamente sobre el modelo sin modificar su estructura, aplicando consultas, automatización y lógica reutilizable.

Se espera que el estudiante:

analice relaciones entre tablas,
construya consultas complejas,
implemente triggers,
cree procedimientos almacenados.
## 5. Restricciones

No está permitido:

modificar la estructura del modelo,
cambiar nombres de tablas o columnas,
crear nuevas entidades.
## 6. Contexto

El sistema de fidelización necesita analizar el comportamiento del cliente y automatizar la acumulación de millas y la asignación de niveles según su actividad.

## 7. Dominios involucrados

Clientes, fidelización, ventas e identidad.

## 8. Problema

Se requiere:

relacionar clientes con su actividad comercial,
automatizar cambios en niveles según acumulación de millas.
## 9. Objetivo

Diseñar una solución que:

conecte ventas con fidelización,
automatice la actualización de niveles,
registre transacciones de millas de forma reutilizable.
## 10. Consulta SQL
SELECT
    CONCAT(p.first_name, ' ', p.last_name) AS nombre_cliente,
    c.customer_since,
    lp.program_name,
    la.account_number,
    lt.tier_name,
    lat.assigned_at,
    s.sale_code,
    s.sold_at
FROM customer c
JOIN person p ON p.person_id = c.person_id
JOIN loyalty_account la ON la.customer_id = c.customer_id
JOIN loyalty_program lp ON lp.loyalty_program_id = la.loyalty_program_id
JOIN loyalty_account_tier lat ON lat.loyalty_account_id = la.loyalty_account_id
JOIN loyalty_tier lt ON lt.loyalty_tier_id = lat.loyalty_tier_id
JOIN reservation r ON r.booked_by_customer_id = c.customer_id
JOIN sale s ON s.reservation_id = r.reservation_id
ORDER BY s.sold_at DESC;
## 11. Trigger
CREATE OR REPLACE FUNCTION fn_update_loyalty_tier()
RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE
    v_total integer;
    v_new_tier uuid;
BEGIN
    SELECT COALESCE(SUM(miles_delta),0)
    INTO v_total
    FROM miles_transaction
    WHERE loyalty_account_id = NEW.loyalty_account_id;

    SELECT lt.loyalty_tier_id
    INTO v_new_tier
    FROM loyalty_account la
    JOIN loyalty_tier lt ON lt.loyalty_program_id = la.loyalty_program_id
    WHERE la.loyalty_account_id = NEW.loyalty_account_id
      AND lt.required_miles <= v_total
    ORDER BY lt.required_miles DESC
    LIMIT 1;

    IF v_new_tier IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM loyalty_account_tier
        WHERE loyalty_account_id = NEW.loyalty_account_id
          AND loyalty_tier_id = v_new_tier
          AND expires_at IS NULL
    ) THEN
        INSERT INTO loyalty_account_tier (
            loyalty_account_id,
            loyalty_tier_id,
            assigned_at,
            expires_at
        )
        VALUES (
            NEW.loyalty_account_id,
            v_new_tier,
            NEW.occurred_at,
            NULL
        );
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_after_miles
AFTER INSERT ON miles_transaction
FOR EACH ROW
EXECUTE FUNCTION fn_update_loyalty_tier();

12. Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_add_miles(
    p_account uuid,
    p_type varchar,
    p_miles integer,
    p_date timestamptz DEFAULT now(),
    p_ref varchar DEFAULT NULL,
    p_note text DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM loyalty_account 
        WHERE loyalty_account_id = p_account
    ) THEN
        RAISE EXCEPTION 'Cuenta no encontrada';
    END IF;

    INSERT INTO miles_transaction (
        loyalty_account_id,
        transaction_type,
        miles_delta,
        occurred_at,
        reference_code,
        notes
    )
    VALUES (
        p_account,
        p_type,
        p_miles,
        p_date,
        p_ref,
        p_note
    );
END;
$$;
13. Script de prueba
DO $$
DECLARE
    v_account uuid;
BEGIN
    SELECT loyalty_account_id 
    INTO v_account
    FROM loyalty_account
    LIMIT 1;

    CALL sp_add_miles(
        v_account,
        'EARN',
        2000,
        now(),
        'TEST-MILES',
        'Carga de prueba'
    );
END;
$$;

SELECT
    la.account_number,
    mt.transaction_type,
    mt.miles_delta,
    lt.tier_name,
    lat.assigned_at
FROM loyalty_account la
JOIN miles_transaction mt ON mt.loyalty_account_id = la.loyalty_account_id
LEFT JOIN loyalty_account_tier lat ON lat.loyalty_account_id = la.loyalty_account_id
LEFT JOIN loyalty_tier lt ON lt.loyalty_tier_id = lat.loyalty_tier_id
ORDER BY mt.created_at DESC
LIMIT 5;