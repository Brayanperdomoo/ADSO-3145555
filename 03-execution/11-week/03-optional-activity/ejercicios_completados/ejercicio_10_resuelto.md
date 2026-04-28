## Solución del Ejercicio 10 - Explicada

## 1. Consulta con INNER JOIN

## ¿Qué hace esta consulta?

Esta consulta une múltiples tablas para mostrar toda la información del pasajero, incluyendo:

identidad
documentos
contactos
reservas

Es una consulta de trazabilidad completa del pasajero.

## Lógica

Se conectan las tablas así:

person → base del pasajero
person_type → tipo de persona
person_document → documentos
document_type → tipo de documento
person_contact → contactos
contact_type → tipo de contacto
reservation_passenger → relación con reservas
reservation → reserva final

## Código
SELECT
    p.first_name || ' ' || p.last_name AS passenger_name,
    pt.type_name AS person_type,
    dt.type_name AS document_type,
    pd.document_number,
    ct.type_name AS contact_type,
    pc.contact_value,
    r.reservation_code,
    rp.passenger_sequence_no
FROM person p
INNER JOIN person_type pt ON pt.person_type_id = p.person_type_id
INNER JOIN person_document pd ON pd.person_id = p.person_id
INNER JOIN document_type dt ON dt.document_type_id = pd.document_type_id
INNER JOIN person_contact pc ON pc.person_id = p.person_id
INNER JOIN contact_type ct ON ct.contact_type_id = pc.contact_type_id
INNER JOIN reservation_passenger rp ON rp.person_id = p.person_id
INNER JOIN reservation r ON r.reservation_id = rp.reservation_id
ORDER BY r.booked_at DESC, rp.passenger_sequence_no;

## 2. Trigger AFTER INSERT OR UPDATE

## ¿Qué hace este trigger?

Cada vez que:

se crea un contacto nuevo
o se actualiza un contacto

👉 automáticamente actualiza la tabla person

🎯 Objetivo

Mantener la trazabilidad de cambios en la identidad del pasajero

🧠 Lógica
Se detecta un cambio en person_contact
Se actualiza el campo updated_at en person
💻 Código
DROP TRIGGER IF EXISTS trg_aiu_person_contact_touch_person ON person_contact;
DROP FUNCTION IF EXISTS fn_aiu_person_contact_touch_person();

CREATE OR REPLACE FUNCTION fn_aiu_person_contact_touch_person()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE person
    SET updated_at = now()
    WHERE person_id = NEW.person_id;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_aiu_person_contact_touch_person
AFTER INSERT OR UPDATE ON person_contact
FOR EACH ROW
EXECUTE FUNCTION fn_aiu_person_contact_touch_person();
🧩 3. Procedimiento almacenado
📌 ¿Qué hace este procedimiento?

Permite:

registrar un nuevo contacto
definir si es principal (primary)
🎯 Objetivo

Centralizar la lógica de contactos del pasajero

🧠 Lógica importante

Si el nuevo contacto es principal:

primero desactiva los anteriores (is_primary = false)
luego inserta el nuevo
💻 Código
CREATE OR REPLACE PROCEDURE sp_register_person_contact(
    p_person_id uuid,
    p_contact_type_id uuid,
    p_contact_value varchar,
    p_is_primary boolean DEFAULT false
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_is_primary THEN
        UPDATE person_contact
        SET is_primary = false, updated_at = now()
        WHERE person_id = p_person_id
          AND contact_type_id = p_contact_type_id;
    END IF;

    INSERT INTO person_contact (person_id, contact_type_id, contact_value, is_primary)
    VALUES (p_person_id, p_contact_type_id, p_contact_value, p_is_primary);
END;
$$;
🧪 4. Script de demostración
📌 ¿Qué hace?
Busca un pasajero
Busca un tipo de contacto
Registra un nuevo contacto
Activa el trigger automáticamente
💻 Código
DO $$
DECLARE
    v_person_id uuid;
    v_contact_type_id uuid;
BEGIN
    SELECT person_id INTO v_person_id FROM person ORDER BY created_at LIMIT 1;
    SELECT contact_type_id INTO v_contact_type_id FROM contact_type ORDER BY created_at LIMIT 1;

    CALL sp_register_person_contact(
        v_person_id,
        v_contact_type_id,
        'demo-' || left(replace(gen_random_uuid()::text, '-', ''), 12) || '@correo.test',
        true
    );
END;
$$;
✅ 5. Consulta de validación
📌 ¿Qué valida?
Que el contacto se creó
Que el trigger actualizó person.updated_at
Que el contacto principal quedó correcto
💻 Código
SELECT
    p.first_name,
    p.last_name,
    p.updated_at,
    ct.type_name,
    pc.contact_value,
    pc.is_primary
FROM person p
INNER JOIN person_contact pc ON pc.person_id = p.person_id
INNER JOIN contact_type ct ON ct.contact_type_id = pc.contact_type_id
ORDER BY pc.created_at DESC
LIMIT 5;
🧠 Resumen claro (para que lo expliques fácil)
🔹 La consulta muestra toda la información del pasajero (identidad + documentos + contacto + reservas)
🔹 El trigger mantiene actualizada la persona cuando cambia su contacto
🔹 El procedimiento permite registrar contactos de forma controlada
🔹 El script prueba todo el flujo
🔹 La validación confirma que todo funcionó