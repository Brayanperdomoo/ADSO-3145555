## Solución del ejercicio 09 - Publicación de tarifas y análisis de reservas comercializadas
## 1. Explicación general de la solución

La solución desarrollada permite analizar el uso de las tarifas dentro del flujo comercial de la aerolínea, conectando la información desde la definición de la tarifa hasta su uso en reservas, ventas y emisión de tiquetes.

Se construyen tres componentes principales:

Consulta SQL con INNER JOIN: permite ver cómo una tarifa se utiliza en el proceso comercial.
Trigger AFTER: automatiza una acción cuando se crea o actualiza una tarifa.
Procedimiento almacenado: encapsula la creación de tarifas.
Script de prueba: demuestra el funcionamiento completo.

Con esto se logra trazabilidad comercial completa y control sobre la publicación de tarifas.

## 2. Consulta con INNER JOIN
Descripción

Esta consulta permite visualizar:

la aerolínea que publica la tarifa
el código de la tarifa
la clase tarifaria
el aeropuerto de origen y destino
la moneda
la reserva asociada
la venta realizada
el tiquete emitido

Esto permite analizar qué tarifas realmente están siendo utilizadas en el negocio.

Consulta SQL
SELECT
    al.airline_name,
    f.fare_code,
    fc.class_code,
    ao.iata_code AS origin_airport,
    ad.iata_code AS destination_airport,
    c.iso_currency_code,
    f.base_amount,
    r.reservation_code,
    s.sale_code,
    t.ticket_number
FROM airline al
INNER JOIN fare f ON f.airline_id = al.airline_id
INNER JOIN fare_class fc ON fc.fare_class_id = f.fare_class_id
INNER JOIN airport ao ON ao.airport_id = f.origin_airport_id
INNER JOIN airport ad ON ad.airport_id = f.destination_airport_id
INNER JOIN currency c ON c.currency_id = f.currency_id
INNER JOIN ticket t ON t.fare_id = f.fare_id
INNER JOIN sale s ON s.sale_id = t.sale_id
INNER JOIN reservation r ON r.reservation_id = s.reservation_id
ORDER BY f.valid_from DESC, f.fare_code;
3. Trigger AFTER INSERT OR UPDATE
Descripción

Este trigger se ejecuta automáticamente cuando:

se inserta una nueva tarifa
o se actualiza una tarifa existente
Lógica implementada

Cada vez que ocurre este evento:

se actualiza el campo updated_at de la tabla airline
esto permite evidenciar que la aerolínea ha realizado cambios en su estructura tarifaria
Implementación
DROP TRIGGER IF EXISTS trg_aiu_fare_touch_airline ON fare;
DROP FUNCTION IF EXISTS fn_aiu_fare_touch_airline();

CREATE OR REPLACE FUNCTION fn_aiu_fare_touch_airline()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE airline
    SET updated_at = now()
    WHERE airline_id = NEW.airline_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_aiu_fare_touch_airline
AFTER INSERT OR UPDATE ON fare
FOR EACH ROW
EXECUTE FUNCTION fn_aiu_fare_touch_airline();

4. Procedimiento almacenado
Descripción

Este procedimiento permite registrar (publicar) una nueva tarifa de forma controlada.

Ventajas
Centraliza la lógica de creación de tarifas
Evita inconsistencias
Es reutilizable desde aplicaciones o scripts
Parámetros principales
aerolínea
aeropuerto origen y destino
clase tarifaria
moneda
valor base
vigencia
condiciones (equipaje, penalidades, etc.)
Implementación
CREATE OR REPLACE PROCEDURE sp_publish_fare(
    p_airline_id uuid,
    p_origin_airport_id uuid,
    p_destination_airport_id uuid,
    p_fare_class_id uuid,
    p_currency_id uuid,
    p_fare_code varchar,
    p_base_amount numeric,
    p_valid_from date,
    p_valid_to date DEFAULT NULL,
    p_baggage_allowance_qty integer DEFAULT 0,
    p_change_penalty_amount numeric DEFAULT NULL,
    p_refund_penalty_amount numeric DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO fare (
        airline_id,
        origin_airport_id,
        destination_airport_id,
        fare_class_id,
        currency_id,
        fare_code,
        base_amount,
        valid_from,
        valid_to,
        baggage_allowance_qty,
        change_penalty_amount,
        refund_penalty_amount
    )
    VALUES (
        p_airline_id,
        p_origin_airport_id,
        p_destination_airport_id,
        p_fare_class_id,
        p_currency_id,
        p_fare_code,
        p_base_amount,
        p_valid_from,
        p_valid_to,
        p_baggage_allowance_qty,
        p_change_penalty_amount,
        p_refund_penalty_amount
    );
END;
$$;
5. Script de prueba
Descripción

Este script permite:

Obtener datos existentes del sistema
Ejecutar el procedimiento almacenado
Insertar una nueva tarifa
Disparar el trigger automáticamente
Script
DO $$
DECLARE
    v_airline_id uuid;
    v_origin_id uuid;
    v_destination_id uuid;
    v_class_id uuid;
    v_currency_id uuid;
BEGIN
    SELECT airline_id INTO v_airline_id FROM airline ORDER BY created_at LIMIT 1;

    SELECT airport_id INTO v_origin_id FROM airport ORDER BY created_at LIMIT 1;

    SELECT airport_id INTO v_destination_id 
    FROM airport 
    WHERE airport_id <> v_origin_id 
    ORDER BY created_at 
    LIMIT 1;

    SELECT fare_class_id INTO v_class_id FROM fare_class ORDER BY created_at LIMIT 1;

    SELECT currency_id INTO v_currency_id FROM currency ORDER BY created_at LIMIT 1;

    CALL sp_publish_fare(
        v_airline_id,
        v_origin_id,
        v_destination_id,
        v_class_id,
        v_currency_id,
        left('FARE-' || replace(gen_random_uuid()::text, '-', ''), 30),
        199.99,
        current_date,
        current_date + 90,
        1,
        40.00,
        60.00
    );
END;
$$;
6. Consulta de validación
Descripción

Esta consulta permite verificar:

que la tarifa fue creada correctamente
que el trigger se ejecutó (actualización en airline)
Consulta
SELECT 
    al.airline_name,
    al.updated_at,
    f.fare_code,
    f.base_amount,
    f.valid_from,
    f.valid_to
FROM fare f
INNER JOIN airline al ON al.airline_id = f.airline_id
ORDER BY f.created_at DESC
LIMIT 5;
7. Conclusión

La solución cumple completamente con los requerimientos del ejercicio:

Se implementó una consulta con múltiples INNER JOIN
Se diseñó un trigger AFTER funcional
Se creó un procedimiento almacenado reutilizable
Se desarrolló un script de prueba completo
Se validó el funcionamiento del flujo

Además, se respetó la estructura del modelo base sin modificaciones, garantizando integridad, consistencia y trazabilidad dentro del sistema.