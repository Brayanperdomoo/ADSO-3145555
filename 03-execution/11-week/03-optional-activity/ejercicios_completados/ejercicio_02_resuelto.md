## Ejercicio 02 - Gestión de pagos y seguimiento de transacciones financieras
Modelo de datos del sistema
## 1. Visión general

El modelo corresponde a una solución integral para una aerolínea, diseñada bajo un esquema relacional que soporta los principales procesos del negocio. Entre ellos se incluyen: información geográfica, gestión de personas, control de seguridad, clientes, programas de fidelización, aeropuertos, aeronaves, operación de vuelos, reservas, emisión de tiquetes, abordaje, pagos y facturación.

El diseño está normalizado y dividido en dominios funcionales, donde las entidades se relacionan mediante claves foráneas, garantizando integridad, consistencia y trazabilidad en todos los procesos.

## 2. Análisis del modelo

Durante la revisión del modelo se identificó que:

contiene más de 60 tablas,
mantiene coherencia en sus relaciones,
aplica restricciones como PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
permite trazabilidad completa desde la venta hasta el pago y facturación.

Esto confirma que se trata de un sistema robusto de tipo empresarial.

## 3. Dominios funcionales
DATOS DE REFERENCIA

Tablas: time_zone, continent, country, state_province, city, district, address, currency
Función: Manejo de ubicaciones y monedas.

AEROLÍNEA

Tablas: airline
Función: Información de la aerolínea.

IDENTIDAD

Tablas: person, document_type, person_document, etc.
Función: Gestión de personas.

SEGURIDAD

Tablas: user_account, roles y permisos
Función: Control de acceso.

CLIENTES

Tablas: customer, loyalty_program, etc.
Función: Fidelización y beneficios.

OPERACIÓN

Tablas: flight, flight_segment
Función: Gestión de vuelos.

VENTAS

Tablas: reservation, sale, ticket
Función: Flujo comercial.

ABORDAJE

Tablas: check_in, boarding_pass
Función: Proceso de embarque.

PAGOS

Tablas: payment, payment_status, payment_method, payment_transaction, refund
Función: Gestión financiera.

FACTURACIÓN

Tablas: invoice
Función: Registro contable.

## 4. Enfoque del ejercicio

El objetivo es trabajar sobre el modelo sin modificar su estructura, desarrollando consultas, automatizaciones y lógica en base de datos.

El estudiante deberá:

construir consultas complejas,
implementar triggers,
desarrollar procedimientos almacenados reutilizables.
## 5. Restricciones

No está permitido:

modificar tablas o columnas,
cambiar relaciones,
crear nuevas entidades.
## 6. Contexto

El área financiera requiere auditar pagos, visualizar sus transacciones asociadas y automatizar el registro de devoluciones cuando ocurra un evento específico.

## 7. Dominios involucrados

Ventas, pagos, facturación y moneda.

## 8. Problema

Se necesita:

consolidar información de ventas y pagos,
automatizar devoluciones cuando haya reversos o eventos similares.
## 9. Objetivo

Diseñar una solución en PostgreSQL que permita:

consultar el flujo financiero,
automatizar acciones sobre devoluciones,
encapsular operaciones mediante procedimientos.
## 10. Consulta SQL
SELECT
    r.reservation_code,
    s.sale_code,
    c.iso_currency_code,
    p.payment_reference,
    ps.status_code AS payment_status,
    pm.method_code AS payment_method,
    p.amount,
    pt.transaction_reference,
    pt.transaction_type,
    pt.transaction_amount,
    pt.processed_at
FROM sale s
JOIN reservation r ON r.reservation_id = s.reservation_id
JOIN currency c ON c.currency_id = s.currency_id
JOIN payment p ON p.sale_id = s.sale_id
JOIN payment_status ps ON ps.payment_status_id = p.payment_status_id
JOIN payment_method pm ON pm.payment_method_id = p.payment_method_id
JOIN payment_transaction pt ON pt.payment_id = p.payment_id
ORDER BY pt.processed_at DESC;
## 11. Trigger
CREATE OR REPLACE FUNCTION fn_handle_refund()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.transaction_type IN ('REFUND','REVERSAL') THEN
        INSERT INTO refund (
            payment_id,
            refund_reference,
            amount,
            requested_at,
            processed_at,
            refund_reason
        )
        VALUES (
            NEW.payment_id,
            'RF-' || substring(replace(NEW.payment_transaction_id::text,'-','') from 1 for 40),
            NEW.transaction_amount,
            NEW.processed_at,
            CASE 
                WHEN NEW.transaction_type = 'REFUND' THEN NEW.processed_at 
                ELSE NULL 
            END,
            COALESCE(NEW.provider_message, 'Generado automáticamente')
        );
    END IF;

    RETURN NEW;
END;
$$;     

CREATE TRIGGER trg_after_payment_tx
AFTER INSERT ON payment_transaction
FOR EACH ROW
EXECUTE FUNCTION fn_handle_refund();
12. Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_add_payment_transaction(
    p_payment_id uuid,
    p_type varchar,
    p_amount numeric,
    p_date timestamptz DEFAULT now(),
    p_message text DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM payment WHERE payment_id = p_payment_id
    ) THEN
        RAISE EXCEPTION 'Pago no encontrado';
    END IF;

    INSERT INTO payment_transaction (
        payment_id,
        transaction_reference,
        transaction_type,
        transaction_amount,
        processed_at,
        provider_message
    )
    VALUES (
        p_payment_id,
        'PT-' || substring(replace(gen_random_uuid()::text,'-','') from 1 for 60),
        p_type,
        p_amount,
        p_date,
        p_message
    );
END;
$$;
13. Script de prueba
DO $$
DECLARE
    v_payment uuid;
    v_amount numeric;
BEGIN
    SELECT payment_id, amount
    INTO v_payment, v_amount
    FROM payment
    LIMIT 1;

    CALL sp_add_payment_transaction(
        v_payment,
        'REFUND',
        v_amount,
        now(),
        'Prueba automática'
    );
END;
$$;

SELECT 
    p.payment_reference,
    pt.transaction_reference,
    pt.transaction_type,
    r.refund_reference,
    r.amount
FROM payment p
JOIN payment_transaction pt ON pt.payment_id = p.payment_id
JOIN refund r ON r.payment_id = p.payment_id
ORDER BY r.created_at DESC
LIMIT 5;