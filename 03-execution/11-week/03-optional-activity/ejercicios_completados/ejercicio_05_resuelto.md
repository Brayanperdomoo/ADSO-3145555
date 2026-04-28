## Solución del ejercicio 05 (Versión alternativa)
Consulta con INNER JOIN
SELECT
    a.registration_number AS aircraft_registration,
    al.airline_name,
    am.model_name,
    mf.manufacturer_name,
    mt.type_name,
    mp.provider_name,
    me.status_code,
    me.started_at,
    me.completed_at
FROM maintenance_event me
INNER JOIN aircraft a ON me.aircraft_id = a.aircraft_id
INNER JOIN airline al ON a.airline_id = al.airline_id
INNER JOIN aircraft_model am ON a.aircraft_model_id = am.aircraft_model_id
INNER JOIN aircraft_manufacturer mf ON am.aircraft_manufacturer_id = mf.aircraft_manufacturer_id
INNER JOIN maintenance_type mt ON me.maintenance_type_id = mt.maintenance_type_id
INNER JOIN maintenance_provider mp ON me.maintenance_provider_id = mp.maintenance_provider_id
ORDER BY me.started_at DESC;
Trigger AFTER sobre maintenance_event
DROP TRIGGER IF EXISTS trg_after_maintenance_event_update_aircraft ON maintenance_event;
DROP FUNCTION IF EXISTS fn_after_maintenance_event_update_aircraft();

CREATE OR REPLACE FUNCTION fn_after_maintenance_event_update_aircraft()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    -- Se actualiza la marca de tiempo de la aeronave cuando ocurre un evento
    UPDATE aircraft
    SET updated_at = now()
    WHERE aircraft_id = NEW.aircraft_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_after_maintenance_event_update_aircraft
AFTER INSERT OR UPDATE ON maintenance_event
FOR EACH ROW
EXECUTE FUNCTION fn_after_maintenance_event_update_aircraft();
Procedimiento almacenado
CREATE OR REPLACE PROCEDURE sp_create_maintenance_event(
    p_aircraft_id uuid,
    p_type_id uuid,
    p_provider_id uuid,
    p_status varchar,
    p_start_date timestamptz,
    p_end_date timestamptz DEFAULT NULL,
    p_notes text DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Inserta un nuevo evento de mantenimiento
    INSERT INTO maintenance_event (
        aircraft_id,
        maintenance_type_id,
        maintenance_provider_id,
        status_code,
        started_at,
        completed_at,
        notes
    )
    VALUES (
        p_aircraft_id,
        p_type_id,
        p_provider_id,
        p_status,
        p_start_date,
        p_end_date,
        p_notes
    );
END;
$$;
Script de prueba
DO $$
DECLARE
    v_aircraft uuid;
    v_type uuid;
    v_provider uuid;
BEGIN
    -- Obtener registros base
    SELECT aircraft_id INTO v_aircraft FROM aircraft LIMIT 1;
    SELECT maintenance_type_id INTO v_type FROM maintenance_type LIMIT 1;
    SELECT maintenance_provider_id INTO v_provider FROM maintenance_provider LIMIT 1;

    -- Ejecutar procedimiento
    CALL sp_create_maintenance_event(
        v_aircraft,
        v_type,
        v_provider,
        'IN_PROGRESS',
        now(),
        NULL,
        'Registro automático de prueba'
    );
END;
$$;

-- Validación del resultado
SELECT 
    a.registration_number,
    me.status_code,
    me.started_at,
    me.completed_at,
    a.updated_at
FROM maintenance_event me
INNER JOIN aircraft a ON me.aircraft_id = a.aircraft_id
ORDER BY me.created_at DESC
LIMIT 5;