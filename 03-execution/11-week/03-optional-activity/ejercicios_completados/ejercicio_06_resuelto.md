Ejercicio 06 - Gestión de retrasos operacionales y análisis por tramo de vuelo
Modelo de datos base del sistema
1. Descripción general del modelo

El modelo de datos corresponde a un sistema integral de aerolínea, diseñado para soportar de forma relacional los procesos principales del negocio: gestión geográfica, identidad de personas, seguridad, clientes, fidelización, aeropuertos, aeronaves, operación de vuelos, reservas, tiquetes, abordaje, pagos y facturación.

Se trata de un modelo robusto y normalizado, en el cual las entidades están organizadas por dominios funcionales y vinculadas mediante llaves foráneas que aseguran trazabilidad, integridad y consistencia a lo largo de todo el flujo operativo y comercial.

2. Resumen previo del análisis realizado

Como parte del análisis inicial, el modelo fue organizado en dominios funcionales, identificando que no se trata de un caso simple, sino de una solución empresarial compleja que integra múltiples áreas del negocio.

Se validó que:

el modelo contiene más de 60 entidades,
las relaciones entre tablas son consistentes,
existen restricciones como PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
el diseño permite trazabilidad completa desde la reserva hasta la facturación.
3. Dominios del modelo y propósito general
GEOGRAPHY AND REFERENCE DATA

Entidades: time_zone, continent, country, state_province, city, district, address, currency
Resumen: Centraliza información geográfica utilizada por todo el sistema.

AIRLINE

Entidades: airline
Resumen: Representa la aerolínea operadora.

IDENTITY

Entidades: person_type, document_type, contact_type, person, person_document, person_contact
Resumen: Gestiona la identidad de las personas.

SECURITY

Entidades: user_status, security_role, security_permission, user_account, user_role, role_permission
Resumen: Controla acceso y permisos del sistema.

CUSTOMER AND LOYALTY

Entidades: customer_category, benefit_type, loyalty_program, loyalty_tier, customer, loyalty_account, loyalty_account_tier, miles_transaction, customer_benefit
Resumen: Maneja clientes y programas de fidelización.

AIRPORT

Entidades: airport, terminal, boarding_gate, runway, airport_regulation
Resumen: Representa la infraestructura aeroportuaria.

AIRCRAFT

Entidades: aircraft_manufacturer, aircraft_model, cabin_class, aircraft, aircraft_cabin, aircraft_seat, maintenance_provider, maintenance_type, maintenance_event
Resumen: Gestiona aeronaves y mantenimiento.

FLIGHT OPERATIONS

Entidades: flight_status, delay_reason_type, flight, flight_segment, flight_delay
Resumen: Controla vuelos, segmentos y retrasos.

SALES, RESERVATION, TICKETING

Entidades: reservation_status, sale_channel, fare_class, fare, ticket_status, reservation, reservation_passenger, sale, ticket, ticket_segment, seat_assignment, baggage
Resumen: Gestiona el flujo comercial.

BOARDING

Entidades: boarding_group, check_in_status, check_in, boarding_pass, boarding_validation
Resumen: Controla el proceso de embarque.

PAYMENT

Entidades: payment_status, payment_method, payment, payment_transaction, refund
Resumen: Administra pagos y devoluciones.

BILLING

Entidades: tax, exchange_rate, invoice_status, invoice, invoice_line
Resumen: Gestiona facturación.

4. Enfoque del ejercicio

Este ejercicio busca que el estudiante analice el flujo operativo de retrasos en vuelos y construya soluciones en PostgreSQL sin modificar la estructura base del modelo.

Se espera que el estudiante:

interprete correctamente las relaciones,
construya consultas complejas,
implemente automatización con triggers,
utilice procedimientos almacenados,
y valide su funcionamiento mediante scripts.
5. Restricción general

No está permitido:

modificar tablas o atributos,
cambiar nombres,
alterar relaciones,
crear entidades nuevas fuera del modelo.
6. Contexto del ejercicio

El área operativa requiere analizar los retrasos en los vuelos y automatizar acciones posteriores cuando se registren demoras en los segmentos.

7. Dominios involucrados
FLIGHT OPERATIONS

Entidades: flight, flight_segment, flight_status, flight_delay, delay_reason_type
Propósito: Gestionar la operación de vuelos y retrasos.

AIRPORT

Entidades: airport
Propósito: Identificar origen y destino.

AIRLINE

Entidades: airline
Propósito: Relacionar vuelos con su aerolínea.

8. Problema a resolver

Se requiere consultar los retrasos por segmento de vuelo y generar un comportamiento automático cuando se registre una demora.

9. Objetivo

Construir una solución que incluya:

consulta multi-tabla,
trigger automático,
procedimiento almacenado.
10. Requerimiento 1 - Consulta SQL
SELECT
    al.airline_name,
    f.flight_number,
    f.service_date,
    fs.segment_number,
    fst.status_code,
    ao.iata_code AS origin,
    ad.iata_code AS destination,
    fd.delay_minutes,
    dr.reason_name,
    fd.reported_at
FROM airline al
INNER JOIN flight f ON f.airline_id = al.airline_id
INNER JOIN flight_status fst ON fst.flight_status_id = f.flight_status_id
INNER JOIN flight_segment fs ON fs.flight_id = f.flight_id
INNER JOIN airport ao ON ao.airport_id = fs.origin_airport_id
INNER JOIN airport ad ON ad.airport_id = fs.destination_airport_id
INNER JOIN flight_delay fd ON fd.flight_segment_id = fs.flight_segment_id
INNER JOIN delay_reason_type dr ON dr.delay_reason_type_id = fd.delay_reason_type_id
ORDER BY fd.reported_at DESC;
11. Requerimiento 2 - Trigger
DROP TRIGGER IF EXISTS trg_after_delay_insert ON flight_delay;
DROP FUNCTION IF EXISTS fn_after_delay_insert();

CREATE OR REPLACE FUNCTION fn_after_delay_insert()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE flight_segment
    SET updated_at = now()
    WHERE flight_segment_id = NEW.flight_segment_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_after_delay_insert
AFTER INSERT ON flight_delay
FOR EACH ROW
EXECUTE FUNCTION fn_after_delay_insert();
12. Requerimiento 3 - Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_create_delay(
    p_segment_id uuid,
    p_reason_id uuid,
    p_minutes integer,
    p_reported_at timestamptz DEFAULT now(),
    p_notes text DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO flight_delay (
        flight_segment_id,
        delay_reason_type_id,
        delay_minutes,
        reported_at,
        notes
    )
    VALUES (
        p_segment_id,
        p_reason_id,
        p_minutes,
        p_reported_at,
        p_notes
    );
END;
$$;
13. Script de prueba
DO $$
DECLARE
    v_segment uuid;
    v_reason uuid;
BEGIN
    SELECT flight_segment_id INTO v_segment FROM flight_segment LIMIT 1;
    SELECT delay_reason_type_id INTO v_reason FROM delay_reason_type LIMIT 1;

    CALL sp_create_delay(v_segment, v_reason, 45, now(), 'Demora de prueba');
END;
$$;

SELECT
    fs.flight_segment_id,
    fs.updated_at,
    fd.delay_minutes,
    fd.notes
FROM flight_segment fs
INNER JOIN flight_delay fd ON fd.flight_segment_id = fs.flight_segment_id
ORDER BY fd.created_at DESC
LIMIT 5;