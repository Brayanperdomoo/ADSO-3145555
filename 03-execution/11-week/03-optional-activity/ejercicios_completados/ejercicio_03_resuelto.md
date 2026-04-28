## Ejercicio 03 - Proceso de facturación y relación entre ventas, impuestos y detalle de factura
Modelo de datos del sistema
## 1. Descripción general

El modelo corresponde a una arquitectura de datos para una aerolínea, diseñada bajo un enfoque relacional que permite soportar los procesos principales del negocio. Entre estos se encuentran: gestión de ubicaciones, manejo de personas, seguridad, clientes, programas de fidelización, infraestructura aeroportuaria, aeronaves, operación de vuelos, reservas, emisión de tiquetes, abordaje, pagos y facturación.

Se trata de un modelo normalizado, organizado por módulos funcionales, donde las entidades están conectadas mediante claves foráneas que garantizan consistencia, integridad y trazabilidad de la información.

## 2. Análisis del modelo

A partir del análisis previo se identificó que:

el sistema contiene más de 60 tablas,
las relaciones entre entidades son coherentes,
se utilizan restricciones como PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
permite trazabilidad completa desde la venta hasta la facturación.

Esto confirma que el modelo está diseñado para un entorno empresarial.

## 3. Dominios funcionales
DATOS GENERALES

Tablas: time_zone, continent, country, state_province, city, district, address, currency
Función: Manejo de ubicaciones y monedas.

AEROLÍNEA

Tablas: airline
Función: Información principal de la empresa.

IDENTIDAD

Tablas: person y relacionadas
Función: Gestión de personas.

SEGURIDAD

Tablas: user_account, roles
Función: Control de acceso.

CLIENTES

Tablas: customer, loyalty
Función: Fidelización.

OPERACIÓN

Tablas: flight, flight_segment
Función: Gestión de vuelos.

VENTAS

Tablas: reservation, sale, ticket
Función: Flujo comercial.

ABORDAJE

Tablas: check_in, boarding_pass
Función: Embarque.

PAGOS

Tablas: payment, payment_transaction, refund
Función: Procesos financieros.

FACTURACIÓN

Tablas: invoice, invoice_line, tax, exchange_rate
Función: Generación de facturas y detalle.

## 4. Enfoque del ejercicio

El ejercicio se centra en trabajar sobre el modelo existente sin modificar su estructura, implementando consultas, automatización y lógica reutilizable en PostgreSQL.

El estudiante debe:

analizar relaciones,
construir consultas complejas,
implementar triggers,
crear procedimientos almacenados.
## 5. Restricciones

No está permitido:

modificar tablas o columnas,
alterar relaciones,
crear nuevas entidades.
## 6. Contexto

El área de facturación necesita validar la coherencia entre las ventas, las facturas generadas y sus líneas, incluyendo los impuestos aplicados.

## 7. Dominios involucrados

Ventas, facturación y moneda.

## 8. Problema

Se requiere:

consultar el detalle facturable de una venta,
automatizar una acción cuando se agreguen nuevas líneas a la factura.
## 9. Objetivo

Construir una solución que:

relacione ventas, facturas e impuestos,
automatice la actualización de la factura,
encapsule la creación de líneas facturables.
## 10. Consulta SQL
SELECT
    s.sale_code,
    i.invoice_number,
    ist.status_code AS invoice_status,
    c.iso_currency_code,
    il.line_number,
    il.line_description,
    il.quantity,
    il.unit_price,
    t.tax_code,
    t.rate_percentage,
    (il.quantity * il.unit_price) AS subtotal
FROM sale s
JOIN invoice i ON i.sale_id = s.sale_id
JOIN invoice_status ist ON ist.invoice_status_id = i.invoice_status_id
JOIN currency c ON c.currency_id = i.currency_id
JOIN invoice_line il ON il.invoice_id = i.invoice_id
JOIN tax t ON t.tax_id = il.tax_id
ORDER BY i.issued_at DESC, il.line_number;
## 11. Trigger
CREATE OR REPLACE FUNCTION fn_update_invoice_after_line()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE invoice
    SET updated_at = now(),
        notes = COALESCE(notes, '') || ' | Línea agregada #' || NEW.line_number
    WHERE invoice_id = NEW.invoice_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_after_invoice_line
AFTER INSERT ON invoice_line
FOR EACH ROW
EXECUTE FUNCTION fn_update_invoice_after_line();
12. Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_create_invoice_line(
    p_invoice uuid,
    p_tax uuid,
    p_line integer,
    p_description varchar,
    p_qty numeric,
    p_price numeric
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM invoice_line 
        WHERE invoice_id = p_invoice AND line_number = p_line
    ) THEN
        RAISE EXCEPTION 'La línea ya existe en la factura';
    END IF;

    INSERT INTO invoice_line (
        invoice_id,
        tax_id,
        line_number,
        line_description,
        quantity,
        unit_price
    )
    VALUES (
        p_invoice,
        p_tax,
        p_line,
        p_description,
        p_qty,
        p_price
    );
END;
$$;
13. Script de prueba
DO $$
DECLARE
    v_invoice uuid;
    v_tax uuid;
    v_line integer;
BEGIN
    SELECT invoice_id INTO v_invoice
    FROM invoice
    LIMIT 1;

    SELECT tax_id INTO v_tax
    FROM tax
    LIMIT 1;

    SELECT COALESCE(MAX(line_number), 0) + 1
    INTO v_line
    FROM invoice_line
    WHERE invoice_id = v_invoice;

    CALL sp_create_invoice_line(
        v_invoice,
        v_tax,
        v_line,
        'Concepto de prueba',
        1,
        50.00
    );
END;
$$;

SELECT 
    i.invoice_number,
    i.updated_at,
    il.line_number,
    il.line_description,
    il.quantity,
    il.unit_price
FROM invoice i
JOIN invoice_line il ON il.invoice_id = i.invoice_id
ORDER BY il.created_at DESC
LIMIT 5;