## Solución del ejercicio 08
Consulta con INNER JOIN

La siguiente consulta permite visualizar de manera integral la relación entre personas, cuentas de usuario, estados, roles asignados y permisos asociados. Esto permite entender cómo se construye el esquema de autorización dentro del sistema.

SELECT
    p.first_name || ' ' || p.last_name AS person_name,
    ua.username,
    us.status_code AS user_status,
    sr.role_code,
    sr.role_name,
    sp.permission_code,
    sp.permission_name,
    ur.assigned_at
FROM person p
INNER JOIN user_account ua ON ua.person_id = p.person_id
INNER JOIN user_status us ON us.user_status_id = ua.user_status_id
INNER JOIN user_role ur ON ur.user_account_id = ua.user_account_id
INNER JOIN security_role sr ON sr.security_role_id = ur.security_role_id
INNER JOIN role_permission rp ON rp.security_role_id = sr.security_role_id
INNER JOIN security_permission sp ON sp.security_permission_id = rp.security_permission_id
ORDER BY ua.username, sr.role_code, sp.permission_code;
Trigger AFTER INSERT sobre user_role

## Este trigger se ejecuta automáticamente después de que se asigna un nuevo rol a un usuario. Su propósito es mantener la trazabilidad del sistema actualizando la fecha de modificación (updated_at) de la cuenta de usuario afectada.

DROP TRIGGER IF EXISTS trg_ai_user_role_touch_account ON user_role;
DROP FUNCTION IF EXISTS fn_ai_user_role_touch_account();

CREATE OR REPLACE FUNCTION fn_ai_user_role_touch_account()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE user_account
    SET updated_at = now()
    WHERE user_account_id = NEW.user_account_id;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_ai_user_role_touch_account
AFTER INSERT ON user_role
FOR EACH ROW
EXECUTE FUNCTION fn_ai_user_role_touch_account();
Procedimiento almacenado

## El siguiente procedimiento permite asignar un rol a un usuario de forma controlada y reutilizable. Además, evita duplicados mediante la cláusula ON CONFLICT.

CREATE OR REPLACE PROCEDURE sp_assign_user_role(
    p_user_account_id uuid,
    p_security_role_id uuid,
    p_assigned_by_user_id uuid DEFAULT NULL,
    p_assigned_at timestamptz DEFAULT now()
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO user_role (user_account_id, security_role_id, assigned_at, assigned_by_user_id)
    VALUES (p_user_account_id, p_security_role_id, p_assigned_at, p_assigned_by_user_id)
    ON CONFLICT (user_account_id, security_role_id) DO NOTHING;
END;
$$;
Script de demostración

## Este script permite probar todo el flujo:

Obtiene identificadores existentes del sistema
Ejecuta el procedimiento almacenado
Dispara el trigger automáticamente
Permite validar los cambios realizados
DO $$
DECLARE
    v_user_id uuid;
    v_role_id uuid;
    v_assigner_id uuid;
BEGIN
    SELECT user_account_id INTO v_user_id FROM user_account ORDER BY created_at LIMIT 1;
    SELECT security_role_id INTO v_role_id FROM security_role ORDER BY created_at LIMIT 1;
    SELECT user_account_id INTO v_assigner_id FROM user_account ORDER BY created_at DESC LIMIT 1;

    CALL sp_assign_user_role(v_user_id, v_role_id, v_assigner_id, now());
END;
$$;
Consulta de validación

Esta consulta permite verificar que el rol fue asignado correctamente y que el trigger actualizó la información de la cuenta de usuario.

SELECT 
    ua.username, 
    ua.updated_at, 
    sr.role_code, 
    ur.assigned_at
FROM user_account ua
INNER JOIN user_role ur ON ur.user_account_id = ua.user_account_id
INNER JOIN security_role sr ON sr.security_role_id = ur.security_role_id
ORDER BY ur.created_at DESC
LIMIT 5;