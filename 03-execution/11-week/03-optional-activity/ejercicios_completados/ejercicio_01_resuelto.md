Ejercicio 01 - Proceso de check-in y seguimiento operativo del pasajero
Modelo de datos del sistema
1. Visión general

El modelo planteado corresponde a una solución integral para la gestión de una aerolínea, estructurada bajo un enfoque relacional que permite soportar los procesos clave del negocio. Entre estos se incluyen: localización geográfica, gestión de personas, seguridad, clientes, programas de fidelización, infraestructura aeroportuaria, aeronaves, operación de vuelos, reservas, emisión de tiquetes, abordaje, pagos y facturación.

El diseño está normalizado y organizado por dominios funcionales, donde cada entidad se relaciona mediante llaves foráneas, garantizando consistencia, integridad y trazabilidad en todos los procesos del sistema.

2. Análisis previo

Durante el análisis inicial, el modelo fue segmentado en distintos dominios funcionales, permitiendo identificar su alcance como una solución empresarial robusta.

Se evidenció que:

el modelo supera las 60 entidades,
existe coherencia en las relaciones entre tablas,
se aplican restricciones como PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
el sistema permite un seguimiento completo desde la reserva hasta la facturación.
3. Dominios funcionales
DATOS GEOGRÁFICOS

Tablas: time_zone, continent, country, state_province, city, district, address, currency
Función: Gestionar información de ubicación y monedas del sistema.

AEROLÍNEA

Tablas: airline
Función: Representar la aerolínea operadora.

IDENTIDAD

Tablas: person_type, document_type, contact_type, person, person_document, person_contact
Función: Administrar información de personas.

SEGURIDAD

Tablas: user_status, security_role, security_permission, user_account, user_role, role_permission
Función: Control de acceso.

CLIENTES Y FIDELIZACIÓN

Tablas: customer_category, benefit_type, loyalty_program, loyalty_tier, customer, loyalty_account, loyalty_account_tier, miles_transaction, customer_benefit
Función: Gestión de clientes y beneficios.

INFRAESTRUCTURA

Tablas: airport, terminal, boarding_gate, runway, airport_regulation
Función: Modelar aeropuertos.

AERONAVES

Tablas: aircraft_manufacturer, aircraft_model, cabin_class, aircraft, aircraft_cabin, aircraft_seat, maintenance_provider, maintenance_type, maintenance_event
Función: Gestión de flota.

OPERACIÓN DE VUELOS

Tablas: flight_status, delay_reason_type, flight, flight_segment, flight_delay
Función: Control de vuelos.

VENTAS Y RESERVAS

Tablas: reservation_status, sale_channel, fare_class, fare, ticket_status, reservation, reservation_passenger, sale, ticket, ticket_segment, seat_assignment, baggage
Función: Flujo comercial.

ABORDAJE

Tablas: boarding_group, check_in_status, check_in, boarding_pass, boarding_validation
Función: Check-in y embarque.

PAGOS

Tablas: payment_status, payment_method, payment, payment_transaction, refund
Función: Transacciones.

FACTURACIÓN

Tablas: tax, exchange_rate, invoice_status, invoice, invoice_line
Función: Facturación.

4. Enfoque del ejercicio

El ejercicio busca que el estudiante analice el modelo y construya soluciones en PostgreSQL sin modificar su estructura.

Debe:

interpretar relaciones,
crear consultas complejas,
implementar triggers,
desarrollar procedimientos almacenados.
5. Restricciones

No se permite:

modificar tablas o columnas,
alterar relaciones,
crear nuevas entidades.
6. Contexto

Se requiere mejorar el control del proceso de abordaje, integrando información desde la reserva hasta el check-in.

7. Dominios involucrados

Ventas, operación de vuelos, identidad, abordaje y seguridad.

8. Problema

Se necesita:

consultar pasajeros por vuelo,
automatizar la generación del boarding pass.
9. Objetivo

Construir una solución en PostgreSQL que permita consulta, automatización y reutilización.

10. Consulta SQL
SELECT
    r.reservation_code,
    f.flight_number,
    f.service_date,
    t.ticket_number,
    rp.passenger_sequence_no,
    CONCAT(p.first_name, ' ', p.last_name) AS passenger_name,
    fs.segment_number,
    fs.scheduled_departure_at
FROM reservation r
JOIN reservation_passenger rp ON rp.reservation_id = r.reservation_id
JOIN person p ON p.person_id = rp.person_id
JOIN ticket t ON t.reservation_passenger_id = rp.reservation_passenger_id
JOIN ticket_segment ts ON ts.ticket_id = t.ticket_id
JOIN flight_segment fs ON fs.flight_segment_id = ts.flight_segment_id
JOIN flight f ON f.flight_id = fs.flight_id
ORDER BY f.service_date, f.flight_number;
11. Trigger
CREATE OR REPLACE FUNCTION fn_generate_boarding_pass()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM boarding_pass 
        WHERE check_in_id = NEW.check_in_id
    ) THEN
        INSERT INTO boarding_pass (
            check_in_id,
            boarding_pass_code,
            barcode_value,
            issued_at
        )
        VALUES (
            NEW.check_in_id,
            'BP-' || substring(replace(NEW.check_in_id::text,'-','') from 1 for 40),
            'BAR-' || NEW.check_in_id,
            NEW.checked_in_at
        );
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_after_checkin
AFTER INSERT ON check_in
FOR EACH ROW
EXECUTE FUNCTION fn_generate_boarding_pass();
12. Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_create_check_in(
    p_ticket_segment uuid,
    p_status uuid,
    p_group uuid,
    p_user uuid
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM check_in 
        WHERE ticket_segment_id = p_ticket_segment
    ) THEN
        RAISE EXCEPTION 'El check-in ya fue registrado';
    END IF;

    INSERT INTO check_in (
        ticket_segment_id,
        check_in_status_id,
        boarding_group_id,
        checked_in_by_user_id,
        checked_in_at
    )
    VALUES (
        p_ticket_segment,
        p_status,
        p_group,
        p_user,
        NOW()
    );
END;
$$;
13. Script de prueba
DO $$
DECLARE
    v_ts uuid;
    v_status uuid;
    v_group uuid;
    v_user uuid;
BEGIN
    SELECT ticket_segment_id INTO v_ts
    FROM ticket_segment
    LIMIT 1;

    SELECT check_in_status_id INTO v_status
    FROM check_in_status
    LIMIT 1;

    SELECT boarding_group_id INTO v_group
    FROM boarding_group
    LIMIT 1;

    SELECT user_account_id INTO v_user
    FROM user_account
    LIMIT 1;

    CALL sp_create_check_in(v_ts, v_status, v_group, v_user);
END;
$$;

SELECT *
FROM boarding_pass
ORDER BY issued_at DESC;