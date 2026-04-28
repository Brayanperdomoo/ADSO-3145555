## Ejercicio 07 - Asignación de asientos y registro de equipaje por segmento ticketed
Modelo de datos base del sistema
## 1. Descripción general del modelo

El modelo de datos corresponde a un sistema integral de aerolínea, diseñado para soportar de forma relacional los procesos principales del negocio: gestión geográfica, identidad de personas, seguridad, clientes, fidelización, aeropuertos, aeronaves, operación de vuelos, reservas, tiquetes, abordaje, pagos y facturación.

Se trata de un modelo amplio y normalizado, en el que las entidades están separadas por dominios funcionales y conectadas mediante llaves foráneas para garantizar trazabilidad, integridad y consistencia en todo el flujo operativo y comercial.

## 2. Resumen previo del análisis realizado

Como base de trabajo, previamente se identificó y organizó el script en dominios funcionales. A partir de esa revisión, se determinó que el modelo no corresponde a un caso pequeño o aislado, sino a una solución empresarial con múltiples áreas del negocio conectadas entre sí.

También se verificó que:

el modelo contiene más de 60 entidades,
las relaciones entre tablas siguen una estructura consistente,
existen restricciones de integridad mediante PRIMARY KEY, FOREIGN KEY, UNIQUE y CHECK,
el diseño soporta trazabilidad end-to-end desde la reserva hasta el pago, abordaje y facturación.
## 3. Dominios del modelo y propósito general
GEOGRAPHY AND REFERENCE DATA

Entidades: time_zone, continent, country, state_province, city, district, address, currency
Resumen: Centraliza información geográfica y de referencia para ubicar aeropuertos, personas, proveedores y definir monedas operativas del sistema.

AIRLINE

Entidades: airline
Resumen: Representa la aerolínea operadora del sistema, incluyendo sus códigos y país base.

IDENTITY

Entidades: person_type, document_type, contact_type, person, person_document, person_contact
Resumen: Permite modelar la identidad de las personas, sus documentos y medios de contacto.

SECURITY

Entidades: user_status, security_role, security_permission, user_account, user_role, role_permission
Resumen: Administra autenticación, autorización y control de acceso al sistema.

CUSTOMER AND LOYALTY

Entidades: customer_category, benefit_type, loyalty_program, loyalty_tier, customer, loyalty_account, loyalty_account_tier, miles_transaction, customer_benefit
Resumen: Gestiona clientes, programas de fidelización, acumulación de millas, beneficios y niveles.

AIRPORT

Entidades: airport, terminal, boarding_gate, runway, airport_regulation
Resumen: Modela la infraestructura aeroportuaria y las condiciones regulatorias asociadas a cada aeropuerto.

AIRCRAFT

Entidades: aircraft_manufacturer, aircraft_model, cabin_class, aircraft, aircraft_cabin, aircraft_seat, maintenance_provider, maintenance_type, maintenance_event
Resumen: Gestiona aeronaves, fabricantes, configuración interna y procesos de mantenimiento.

FLIGHT OPERATIONS

Entidades: flight_status, delay_reason_type, flight, flight_segment, flight_delay
Resumen: Controla la operación de vuelos, sus segmentos, estados y retrasos.

SALES, RESERVATION, TICKETING

Entidades: reservation_status, sale_channel, fare_class, fare, ticket_status, reservation, reservation_passenger, sale, ticket, ticket_segment, seat_assignment, baggage
Resumen: Gestiona el flujo comercial principal: reserva, pasajero, venta, emisión de tiquetes, asignación de asiento y equipaje.

BOARDING

Entidades: boarding_group, check_in_status, check_in, boarding_pass, boarding_validation
Resumen: Soporta el proceso de check-in, emisión de pase de abordar y validación final de embarque.

PAYMENT

Entidades: payment_status, payment_method, payment, payment_transaction, refund
Resumen: Administra pagos, transacciones y devoluciones asociadas a las ventas.

BILLING

Entidades: tax, exchange_rate, invoice_status, invoice, invoice_line
Resumen: Gestiona impuestos, tasas de cambio, facturas y detalle facturable.

## 4. Enfoque de los ejercicios

Los ejercicios planteados sobre este modelo tendrán como propósito que el estudiante analice relaciones reales entre entidades y construya soluciones en PostgreSQL sin alterar la estructura base del sistema.

Cada ejercicio se formulará para que el estudiante:

interprete correctamente los dominios involucrados,
construya consultas con múltiples relaciones,
diseñe automatizaciones con triggers,
implemente lógica reutilizable mediante procedimientos almacenados,
y demuestre técnicamente el funcionamiento con scripts de prueba.

## 5. Restricción general para todos los ejercicios

Todos los ejercicios deben resolverse respetando estrictamente el modelo entregado.

No está permitido:

cambiar atributos existentes,
renombrar tablas o columnas,
alterar relaciones,
inventar entidades fuera del script base,
ni modificar la estructura general del modelo.

La solución deberá construirse únicamente sobre las entidades y relaciones reales definidas en el script.

## 6. Contexto del ejercicio

El área de aeropuerto necesita auditar qué asientos y equipajes están asociados a cada segmento ticketed y automatizar una acción posterior cuando se registre un equipaje o una asignación operativa.

## 7. Dominios involucrados en este ejercicio
SALES, RESERVATION, TICKETING

Entidades: ticket, ticket_segment, seat_assignment, baggage
Propósito: Gestionar el tiquete, su segmento, la asignación de asiento y el equipaje asociado.

AIRCRAFT

Entidades: aircraft, aircraft_cabin, aircraft_seat, cabin_class
Propósito: Relacionar el asiento con la configuración real de la aeronave.

FLIGHT OPERATIONS

Entidades: flight, flight_segment
Propósito: Relacionar el segmento ticketed con el segmento operativo del vuelo.

## 8. Planteamiento del problema

Se requiere consultar de manera integrada la asignación de asientos y el registro de equipaje por pasajero y segmento, y además automatizar una reacción posterior cuando ocurra un evento sobre equipaje o asiento.

## 9. Objetivo del ejercicio

Definir un ejercicio de trazabilidad aeroportuaria que combine consulta multi-tabla, trigger posterior y procedimiento almacenado en torno a asientos y equipaje.

## 10. Requerimiento 1 - Consulta con INNER JOIN de al menos 5 tablas
Enunciado

Construya una consulta SQL que relacione tiquete, segmento ticketed, segmento operativo, asiento asignado, cabina y equipaje registrado.

Restricciones obligatorias
Debe usar INNER JOIN
Debe involucrar mínimo 5 tablas
Debe usar únicamente entidades y atributos existentes en el modelo base
No se permite cambiar nombres de tablas ni columnas
La consulta debe ser coherente con las relaciones reales del modelo
Resultado esperado

Mostrar por cada segmento ticketed el asiento asignado, cabina y equipaje del pasajero.

Solución del ejercicio 07
Consulta con INNER JOIN
SELECT
    t.ticket_number,
    ts.segment_sequence_no,
    f.flight_number,
    fs.segment_number,
    cc.class_name,
    aseat.seat_row_number || aseat.seat_column_code AS seat_code,
    sa.assignment_source,
    b.baggage_tag,
    b.baggage_type,
    b.baggage_status,
    b.weight_kg
FROM ticket t
INNER JOIN ticket_segment ts ON ts.ticket_id = t.ticket_id
INNER JOIN flight_segment fs ON fs.flight_segment_id = ts.flight_segment_id
INNER JOIN flight f ON f.flight_id = fs.flight_id
INNER JOIN seat_assignment sa ON sa.ticket_segment_id = ts.ticket_segment_id
INNER JOIN aircraft_seat aseat ON aseat.aircraft_seat_id = sa.aircraft_seat_id
INNER JOIN aircraft_cabin ac ON ac.aircraft_cabin_id = aseat.aircraft_cabin_id
INNER JOIN cabin_class cc ON cc.cabin_class_id = ac.cabin_class_id
INNER JOIN baggage b ON b.ticket_segment_id = ts.ticket_segment_id
ORDER BY f.service_date DESC, t.ticket_number;
Trigger AFTER INSERT sobre baggage
Explicación

Cuando se registra un equipaje sin fecha de check-in (checked_at), el sistema asigna automáticamente la fecha actual.

DROP TRIGGER IF EXISTS trg_ai_baggage_default_checked_at ON baggage;
DROP FUNCTION IF EXISTS fn_ai_baggage_default_checked_at();

CREATE OR REPLACE FUNCTION fn_ai_baggage_default_checked_at()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.checked_at IS NULL THEN
        UPDATE baggage
        SET checked_at = now(), updated_at = now()
        WHERE baggage_id = NEW.baggage_id;
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_ai_baggage_default_checked_at
AFTER INSERT ON baggage
FOR EACH ROW
EXECUTE FUNCTION fn_ai_baggage_default_checked_at();
Procedimiento almacenado
Explicación

Permite registrar equipaje de forma reutilizable para un segmento ticketed.

CREATE OR REPLACE PROCEDURE sp_register_baggage(
    p_ticket_segment_id uuid,
    p_baggage_tag varchar,
    p_baggage_type varchar,
    p_baggage_status varchar,
    p_weight_kg numeric,
    p_checked_at timestamptz DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO baggage (
        ticket_segment_id,
        baggage_tag,
        baggage_type,
        baggage_status,
        weight_kg,
        checked_at
    )
    VALUES (
        p_ticket_segment_id,
        p_baggage_tag,
        p_baggage_type,
        p_baggage_status,
        p_weight_kg,
        p_checked_at
    );
END;
$$;
Script de demostración
Explicación
Obtiene un ticket_segment
Registra equipaje
Activa el trigger automáticamente
Permite validar el resultado
DO $$
DECLARE
    v_ticket_segment_id uuid;
BEGIN
    SELECT ticket_segment_id
    INTO v_ticket_segment_id
    FROM ticket_segment
    ORDER BY created_at
    LIMIT 1;

    CALL sp_register_baggage(
        v_ticket_segment_id,
        left('BAG-' || replace(gen_random_uuid()::text, '-', ''), 30),
        'CHECKED',
        'REGISTERED',
        18.50,
        NULL
    );
END;
$$;

SELECT
    b.baggage_tag,
    b.baggage_status,
    b.weight_kg,
    b.checked_at
FROM baggage b
ORDER BY b.created_at DESC
LIMIT 5;