DROP FUNCTION IF EXISTS obtener_nombre(UUID);

CREATE OR REPLACE FUNCTION obtener_nombre(p_person_id UUID)
RETURNS VARCHAR
LANGUAGE plpgsql
AS $$
DECLARE
    full_name VARCHAR;
BEGIN
    SELECT TRIM(first_name || ' ' || last_name)
    INTO full_name
    FROM person
    WHERE id = p_person_id
      AND status = TRUE;

    RETURN COALESCE(full_name, 'NO ENCONTRADO');
END;
$$;

-- ejemplo de uso
SELECT obtener_nombre('123e4567-e89b-12d3-a456-426614174000');