# Módulo de Seguridad - Escenarios de Uso

## Descripción del Sistema de Seguridad

El módulo de seguridad del sistema Coffee Shop implementa un modelo **RBAC (Role-Based Access Control)** mediante el cual se controlan los permisos de acceso de los usuarios a los diferentes recursos del sistema.

### Estructura del Sistema

```
Usuario → Rol → Módulo → Vista
```

- **user**: Representa a los usuarios del sistema
- **role**: Define roles organizacionales (Admin, Manager, Cashier, etc.)
- **module**: Agrupa funcionalidades relacionadas (Security, Inventory, Sales, etc.)
- **view**: Representa pantallas o acciones específicas dentro de un módulo
- **user_role**: Relación muchos a muchos entre usuarios y roles
- **role_module**: Relación muchos a muchos entre roles y módulos
- **module_view**: Relación muchos a muchos entre módulos y vistas

### Control de Permisos

El sistema verifica permisos mediante la siguiente consulta lógica:

```sql
¿El usuario tiene un rol activo?
  → ¿Ese rol tiene acceso al módulo solicitado?
    → ¿Ese módulo contiene la vista solicitada?
      → ¿Todos los registros están activos (deleted_at IS NULL)?
        → PERMITIR ACCESO
```

---

## Escenario 1: Autenticación y Carga de Permisos

### Contexto
Un usuario intenta iniciar sesión en el sistema Coffee Shop.

### Precondiciones
- El usuario existe en la base de datos
- El usuario tiene al menos un rol asignado
- Las credenciales son correctas

### Flujo Principal

1. **Usuario** ingresa username y password en el formulario de login
2. **Sistema** valida las credenciales contra la tabla `user`
3. **Sistema** consulta los roles del usuario:
   ```sql
   SELECT r.* 
   FROM role r
   INNER JOIN user_role ur ON r.id = ur.role_id
   WHERE ur.user_id = :userId
     AND ur.deleted_at IS NULL
     AND ur.status = 'active'
   ```
4. **Sistema** consulta los módulos accesibles según los roles:
   ```sql
   SELECT DISTINCT m.* 
   FROM module m
   INNER JOIN role_module rm ON m.id = rm.module_id
   WHERE rm.role_id IN (:roleIds)
     AND rm.deleted_at IS NULL
     AND rm.status = 'active'
   ```
5. **Sistema** consulta las vistas accesibles según los módulos:
   ```sql
   SELECT DISTINCT v.* 
   FROM view v
   INNER JOIN module_view mv ON v.id = mv.view_id
   WHERE mv.module_id IN (:moduleIds)
     AND mv.deleted_at IS NULL
     AND mv.status = 'active'
   ```
6. **Sistema** genera un token JWT con la información de permisos
7. **Usuario** recibe el token y el menú personalizado según sus permisos

### Resultado Esperado
- Token JWT generado con permisos embebidos
- Frontend muestra solo las opciones de menú permitidas
- Usuario redirigido al dashboard principal

### Datos de Ejemplo

**Usuario:** `maria.garcia@coffeeshop.com`

**Roles asignados:**
- Manager (id: `role-001`)

**Módulos accesibles:**
- Inventory (id: `mod-003`)
- Sales (id: `mod-004`)

**Vistas accesibles:**
- `/inventory/products` (Product List)
- `/inventory/categories` (Category Management)
- `/sales/orders` (Order Management)
- `/sales/customers` (Customer List)

---

## Escenario 2: Verificación de Permisos en Tiempo Real

### Contexto
Un usuario autenticado intenta acceder a una vista específica del sistema.

### Precondiciones
- Usuario ha iniciado sesión correctamente
- Usuario posee un token JWT válido
- Sistema está en funcionamiento

### Flujo Principal

1. **Usuario** hace clic en el menú "Gestión de Productos" (`/inventory/products`)
2. **Frontend** envía petición GET al backend con el token JWT:
   ```http
   GET /api/inventory/products
   Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
   ```
3. **Middleware de Autenticación** valida el token JWT
4. **Middleware de Autorización** verifica permisos:
   ```sql
   SELECT COUNT(*) AS has_permission
   FROM user_role ur
   INNER JOIN role_module rm ON ur.role_id = rm.role_id
   INNER JOIN module_view mv ON rm.module_id = mv.module_id
   INNER JOIN view v ON mv.view_id = v.id
   WHERE ur.user_id = :userId
     AND v.route = '/inventory/products'
     AND ur.deleted_at IS NULL
     AND rm.deleted_at IS NULL
     AND mv.deleted_at IS NULL
     AND ur.status = 'active'
   ```
5. Si `has_permission > 0`, la petición continúa al controlador
6. **Controlador** procesa la petición y retorna los datos
7. **Frontend** muestra la lista de productos

### Resultado Esperado
- Acceso concedido si el usuario tiene permisos
- Código HTTP 200 OK con los datos solicitados
- Código HTTP 403 Forbidden si no tiene permisos

### Flujo Alternativo: Sin Permisos

4a. La consulta SQL retorna `has_permission = 0`
5a. **Sistema** retorna HTTP 403 Forbidden
6a. **Frontend** muestra mensaje: "No tienes permisos para acceder a este recurso"

---

## Escenario 3: Asignación de Roles a un Nuevo Usuario

### Contexto
Un administrador necesita asignar roles a un usuario recién creado.

### Precondiciones
- Existe un usuario administrador con permisos de gestión de usuarios
- El nuevo usuario ya existe en la tabla `user`
- Existen roles disponibles en la tabla `role`

### Flujo Principal

1. **Administrador** accede a `/security/users`
2. **Administrador** busca y selecciona el usuario "Juan Pérez"
3. **Sistema** carga los detalles del usuario y muestra los roles disponibles
4. **Administrador** selecciona los roles:
   - ☑ Cashier
   - ☑ Warehouse Staff
5. **Administrador** hace clic en "Asignar Roles"
6. **Frontend** envía petición:
   ```http
   POST /api/users/uuid-juan-perez/roles
   {
     "roleIds": ["role-cashier-uuid", "role-warehouse-uuid"]
   }
   ```
7. **Sistema** verifica que el administrador tenga permiso para gestionar usuarios
8. **Sistema** inserta registros en `user_role`:
   ```sql
   INSERT INTO user_role (id, user_id, role_id, created_at, created_by, status)
   VALUES 
     (gen_random_uuid(), 'uuid-juan-perez', 'role-cashier-uuid', NOW(), 'uuid-admin', 'active'),
     (gen_random_uuid(), 'uuid-juan-perez', 'role-warehouse-uuid', NOW(), 'uuid-admin', 'active');
   ```
9. **Sistema** retorna los roles asignados
10. **Frontend** muestra confirmación y actualiza la vista

### Resultado Esperado
- Roles asignados correctamente en `user_role`
- Auditoría registrada (`created_at`, `created_by`)
- Usuario puede iniciar sesión con los nuevos permisos

### Datos Después de la Operación

**Tabla `user_role`:**
| id | user_id | role_id | created_at | created_by | status |
|----|---------|---------|------------|------------|--------|
| ur-001 | juan-uuid | cashier-uuid | 2026-03-04 10:30:00 | admin-uuid | active |
| ur-002 | juan-uuid | warehouse-uuid | 2026-03-04 10:30:00 | admin-uuid | active |

---

## Escenario 4: Configuración de Permisos para un Nuevo Rol

### Contexto
Se necesita crear un nuevo rol "Inventory Manager" con acceso específico al módulo de inventario.

### Precondiciones
- Usuario administrador autenticado
- Módulo "Inventory" existe con sus vistas configuradas
- El nuevo rol ya fue creado en la tabla `role`

### Flujo Principal

1. **Administrador** accede a `/security/roles`
2. **Administrador** selecciona el rol "Inventory Manager"
3. **Sistema** muestra los módulos disponibles y sus vistas
4. **Administrador** selecciona permisos:
   
   **Módulo: Inventory**
   - ☑ Product List (View)
   - ☑ Product Create (View)
   - ☑ Product Edit (View)
   - ☑ Category Management (View)
   - ☑ Inventory Control (View)
   - ☐ Supplier Management (View) ← No seleccionado

5. **Administrador** hace clic en "Guardar Permisos"
6. **Sistema** procesa la asignación:
   
   **Paso 1:** Asignar módulo al rol
   ```sql
   INSERT INTO role_module (id, role_id, module_id, created_at, created_by, status)
   VALUES (gen_random_uuid(), 'inventory-manager-uuid', 'inventory-module-uuid', NOW(), 'admin-uuid', 'active');
   ```
   
   **Paso 2:** Las vistas ya están asociadas al módulo en `module_view`
   ```sql
   -- Estas relaciones ya existen:
   SELECT * FROM module_view WHERE module_id = 'inventory-module-uuid';
   ```

7. **Sistema** retorna confirmación
8. **Frontend** muestra mensaje: "Permisos configurados correctamente"

### Resultado Esperado
- Rol tiene acceso al módulo Inventory
- Usuarios con este rol pueden acceder a las vistas del módulo
- El control granular se realiza validando la cadena: user → role → module → view

### Validación

Un usuario con el rol "Inventory Manager" puede acceder a:
- ✅ `/inventory/products`
- ✅ `/inventory/categories`
- ✅ `/inventory/stock`
- ❌ `/inventory/suppliers` (no está en las vistas permitidas para este flujo)

---

## Escenario 5: Revocación de Acceso (Soft Delete)

### Contexto
Un empleado cambia de departamento y necesita que se le revoquen ciertos permisos sin eliminar su historial.

### Precondiciones
- Usuario "Carlos Mendoza" tiene múltiples roles asignados
- Se utiliza soft delete (`deleted_at`, `deleted_by`)
- El administrador tiene permisos para gestionar usuarios

### Flujo Principal

1. **Administrador** accede a `/security/users`
2. **Administrador** busca "Carlos Mendoza"
3. **Sistema** muestra los roles actuales:
   - ☑ Manager (Activo)
   - ☑ Cashier (Activo)
   - ☑ Warehouse Staff (Activo)
4. **Administrador** decide revocar el rol "Cashier"
5. **Administrador** desmarca "Cashier" y guarda
6. **Frontend** envía petición:
   ```http
   DELETE /api/users/carlos-uuid/roles/cashier-uuid
   ```
7. **Sistema** realiza soft delete:
   ```sql
   UPDATE user_role 
   SET deleted_at = NOW(),
       deleted_by = 'admin-uuid',
       status = 'inactive'
   WHERE user_id = 'carlos-uuid' 
     AND role_id = 'cashier-uuid';
   ```
8. **Sistema** retorna confirmación
9. **Frontend** actualiza la lista de roles

### Resultado Esperado
- El registro en `user_role` NO se elimina físicamente
- `deleted_at` tiene timestamp
- `deleted_by` registra quién hizo el cambio
- `status` cambia a 'inactive'
- El usuario ya no tiene acceso a vistas del módulo Sales (asociadas a Cashier)

### Estado de la Tabla `user_role` Después

| id | user_id | role_id | created_at | deleted_at | deleted_by | status |
|----|---------|---------|------------|------------|------------|--------|
| ur-100 | carlos-uuid | manager-uuid | 2026-01-15 | NULL | NULL | active |
| ur-101 | carlos-uuid | cashier-uuid | 2026-01-15 | **2026-03-04 14:20:00** | **admin-uuid** | **inactive** |
| ur-102 | carlos-uuid | warehouse-uuid | 2026-01-15 | NULL | NULL | active |

### Validación de Permisos

Cuando Carlos intenta acceder a `/sales/orders`:

```sql
SELECT COUNT(*) 
FROM user_role ur
WHERE ur.user_id = 'carlos-uuid'
  AND ur.role_id = 'cashier-uuid'
  AND ur.deleted_at IS NULL  -- ❌ FALLA: deleted_at = 2026-03-04
  AND ur.status = 'active'   -- ❌ FALLA: status = 'inactive'
```

**Resultado:** `COUNT = 0` → **Acceso Denegado (403 Forbidden)**

---

## Escenario 6: Auditoría de Cambios de Permisos

### Contexto
El equipo de seguridad necesita auditar quién modificó los permisos de un usuario específico.

### Precondiciones
- Todos los cambios registran `created_by`, `updated_by`, `deleted_by`
- Se mantiene historial completo con soft deletes

### Flujo Principal

1. **Auditor** accede al panel de auditoría
2. **Auditor** busca cambios para el usuario "Ana Torres"
3. **Sistema** ejecuta consulta de auditoría:
   ```sql
   SELECT 
     ur.id,
     u.username,
     r.name AS role_name,
     ur.created_at,
     creator.username AS created_by_user,
     ur.deleted_at,
     deleter.username AS deleted_by_user,
     ur.status
   FROM user_role ur
   INNER JOIN user u ON ur.user_id = u.id
   INNER JOIN role r ON ur.role_id = r.id
   LEFT JOIN user creator ON ur.created_by = creator.id
   LEFT JOIN user deleter ON ur.deleted_by = deleter.id
   WHERE u.username = 'ana.torres'
   ORDER BY ur.created_at DESC;
   ```

4. **Sistema** retorna historial completo:

### Resultado Esperado

| Rol | Asignado el | Asignado por | Revocado el | Revocado por | Estado |
|-----|-------------|--------------|-------------|--------------|--------|
| Administrator | 2026-01-10 08:00 | system.admin | - | - | Activo |
| Manager | 2026-02-15 10:30 | juan.perez | 2026-03-01 14:00 | system.admin | Revocado |
| Cashier | 2026-03-02 09:15 | maria.garcia | - | - | Activo |

5. **Auditor** puede identificar:
   - Quién asignó cada rol
   - Cuándo se asignó
   - Quién revocó permisos
   - Cuándo se revocó
   - Estado actual

### Información Adicional para Auditoría

```sql
-- Consulta de cambios recientes en módulos
SELECT 
  rm.*,
  r.name AS role_name,
  m.name AS module_name,
  creator.username AS assigned_by
FROM role_module rm
INNER JOIN role r ON rm.role_id = r.id
INNER JOIN module m ON rm.module_id = m.id
LEFT JOIN user creator ON rm.created_by = creator.id
WHERE rm.created_at >= NOW() - INTERVAL '30 days'
ORDER BY rm.created_at DESC;
```

---

## Control de Permisos - Resumen Técnico

### Niveles de Verificación

1. **Nivel 1: Autenticación**
   - Validación de JWT
   - Verificación de expiración del token
   - Extracción de `userId`

2. **Nivel 2: Autorización de Rol**
   ```sql
   user → user_role → role (WHERE deleted_at IS NULL AND status = 'active')
   ```

3. **Nivel 3: Autorización de Módulo**
   ```sql
   role → role_module → module (WHERE deleted_at IS NULL AND status = 'active')
   ```

4. **Nivel 4: Autorización de Vista**
   ```sql
   module → module_view → view (WHERE deleted_at IS NULL AND route = :requestedRoute)
   ```

### Consulta Completa de Verificación

```sql
SELECT 
  v.id,
  v.name,
  v.route,
  v.description
FROM user u
INNER JOIN user_role ur ON u.id = ur.user_id
INNER JOIN role r ON ur.role_id = r.id
INNER JOIN role_module rm ON r.id = rm.role_id
INNER JOIN module m ON rm.module_id = m.id
INNER JOIN module_view mv ON m.id = mv.module_id
INNER JOIN view v ON mv.view_id = v.id
WHERE u.id = :userId
  AND v.route = :requestedRoute
  AND ur.deleted_at IS NULL
  AND ur.status = 'active'
  AND rm.deleted_at IS NULL
  AND rm.status = 'active'
  AND mv.deleted_at IS NULL
  AND mv.status = 'active'
LIMIT 1;
```

### Códigos de Respuesta HTTP

- **200 OK**: Acceso permitido, recurso entregado
- **401 Unauthorized**: Token inválido o expirado
- **403 Forbidden**: Token válido pero sin permisos para el recurso
- **404 Not Found**: Recurso no existe

---

## Escenario 7: Herencia de Permisos por Jerarquía de Roles

### Contexto
Se implementa una jerarquía donde ciertos roles heredan permisos de otros.

### Diseño Propuesto

```
Administrator (Todos los permisos)
    ↓
Manager (Inventory + Sales + Reports)
    ↓
Cashier (Solo Sales)
    ↓
Warehouse Staff (Solo Inventory)
```

### Implementación

**Opción A:** Asignación explícita (actual)
- Cada rol se asigna explícitamente a módulos
- No hay herencia automática
- Mayor control granular

**Opción B:** Herencia mediante tabla adicional
```sql
CREATE TABLE role_hierarchy (
  id UUID PRIMARY KEY,
  parent_role_id UUID REFERENCES role(id),
  child_role_id UUID REFERENCES role(id)
);
```

### Flujo con Herencia

1. Usuario tiene rol "Manager"
2. Sistema consulta permisos directos de "Manager"
3. Sistema consulta roles padres en `role_hierarchy`
4. Sistema agrega permisos de roles padres
5. Usuario obtiene permisos combinados

### Consulta con Herencia

```sql
WITH RECURSIVE role_chain AS (
  -- Roles directos del usuario
  SELECT r.id, r.name
  FROM role r
  INNER JOIN user_role ur ON r.id = ur.role_id
  WHERE ur.user_id = :userId
    AND ur.deleted_at IS NULL
  
  UNION ALL
  
  -- Roles padres heredados
  SELECT parent.id, parent.name
  FROM role parent
  INNER JOIN role_hierarchy rh ON parent.id = rh.parent_role_id
  INNER JOIN role_chain child ON rh.child_role_id = child.id
)
SELECT DISTINCT v.*
FROM role_chain rc
INNER JOIN role_module rm ON rc.id = rm.role_id
INNER JOIN module_view mv ON rm.module_id = mv.module_id
INNER JOIN view v ON mv.view_id = v.id
WHERE rm.deleted_at IS NULL;
```

---

## Conclusiones

El sistema de seguridad implementado proporciona:

✅ **Control granular** mediante la cadena User → Role → Module → View  
✅ **Auditoría completa** con timestamps y referencias de usuario  
✅ **Soft delete** para mantener historial sin pérdida de datos  
✅ **Flexibilidad** para asignar múltiples roles a usuarios  
✅ **Escalabilidad** mediante relaciones muchos a muchos  
✅ **Seguridad** con validación en cada capa de la aplicación  

### Mejores Prácticas Implementadas

1. **Principio de mínimo privilegio**: Usuarios solo acceden a lo necesario
2. **Separación de responsabilidades**: Roles claramente definidos
3. **Trazabilidad**: Registro completo de cambios
4. **Reversibilidad**: Soft delete permite restaurar permisos
5. **Validación en múltiples capas**: Frontend, Middleware, Backend
