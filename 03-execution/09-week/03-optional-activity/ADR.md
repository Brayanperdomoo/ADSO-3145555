# Registro de Decisiones Arquitectónicas – Esquema de Base de Datos

> Este registro documenta los cambios deliberados realizados sobre el modelo de datos, incluyendo el problema que los motivó, la solución elegida, las opciones que se descartaron y el impacto que se espera de cada cambio.

---

## ADR-001 · seat_assignment – Eliminación de FK compuesta redundante

**Problema identificado**
La tabla `seat_assignment` mantenía una clave foránea compuesta sobre `(ticket_segment_id, flight_segment_id)`. Dado que `ticket_segment_id` por sí solo ya referencia de forma unívoca al registro en `ticket_segment`, el segundo campo en la composición no añadía ninguna garantía adicional de integridad.

**Solución adoptada**
Se elimina la FK compuesta y se conserva únicamente la relación directa a través de `ticket_segment_id`.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Conservar la FK compuesta | Complejidad innecesaria sin beneficio real |
| Redefinir `ticket_segment` con clave compuesta | Impacto mayor al problema que resuelve |

**Impacto esperado**
El modelo gana legibilidad y las consultas se simplifican. La integridad sigue garantizada por la PK de `ticket_segment`.

---

## ADR-002 · seat_assignment – Nueva FK hacia flight_segment

**Problema identificado**
El campo `flight_segment_id` en `seat_assignment` no tenía ninguna restricción referencial. Era posible persistir un valor que no correspondiera a ningún segmento de vuelo real, introduciendo registros silenciosamente inválidos.

**Solución adoptada**
Se agrega una clave foránea directa desde `seat_assignment.flight_segment_id` hacia `flight_segment`.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Validar solo desde el backend | El modelo de datos no debería depender exclusivamente de la aplicación para garantizar su consistencia |
| Dejar el campo sin restricción | Riesgo de datos corruptos en producción |

**Impacto esperado**
La base de datos rechaza activamente asignaciones de asiento ligadas a vuelos inexistentes. La capa de datos asume responsabilidad directa sobre esta regla.

---

## ADR-003 · seat_assignment – Coherencia entre asiento y aeronave del vuelo

**Problema identificado**
Un asiento y un segmento de vuelo pueden existir de forma independiente en el sistema, pero no necesariamente corresponder al mismo avión. Sin una validación cruzada, es posible asignar un asiento de una aeronave diferente a la que opera el vuelo.

**Solución adoptada**
Se introduce una validación que verifica que el asiento asignado pertenezca a la misma aeronave referenciada por el segmento de vuelo. La implementación puede residir en un trigger de base de datos, en el backend, o en ambos niveles.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| No validar | Riesgo operativo crítico |
| Solo backend | Insuficiente si hay acceso directo a la BD |

**Impacto esperado**
Se eliminan asignaciones operativamente imposibles. Queda pendiente definir si la validación vivirá en la base de datos, en el backend o en ambas capas simultáneamente.

---

## ADR-004 · Unicidad condicional en campos opcionales

**Problema identificado**
Varios campos opcionales contaban con restricciones `UNIQUE` estándar. En PostgreSQL, este tipo de restricción admite múltiples `NULL` simultáneos, lo que puede generar comportamientos inesperados cuando se quiere aplicar unicidad solo sobre valores presentes.

**Solución adoptada**
Se reemplazan las restricciones `UNIQUE` convencionales por índices únicos parciales con la condición `WHERE campo IS NOT NULL`:

```sql
CREATE UNIQUE INDEX idx_unique_optional_field
  ON table_name(optional_field)
  WHERE optional_field IS NOT NULL;
```

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Mantener `UNIQUE` estándar | Comportamiento ambiguo con NULLs |
| Convertir campos a `NOT NULL` | Rompe la intención de opcionalidad del campo |

**Impacto esperado**
La unicidad opera exactamente donde se necesita. El esquema documenta explícitamente cómo se espera que se comporte PostgreSQL con estos campos.

---

## ADR-005 · password_hash – Tipo de dato y longitud mínima

**Problema identificado**
La columna `password_hash` estaba definida como `VARCHAR(255)`, un límite que ciertos algoritmos de hashing pueden exceder. Además, no existía ninguna validación en base de datos que garantizara que el valor almacenado tuviese al menos una longitud representativa de un hash real.

**Solución adoptada**
Se cambia el tipo de la columna a `TEXT` y se agrega una restricción de longitud mínima directamente en el esquema.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Mantener `VARCHAR(255)` | Puede truncar hashes de algoritmos modernos |
| Validar solo desde el backend | El esquema quedaría sin protección ante escrituras directas |

**Impacto esperado**
El esquema tolera distintos algoritmos de hashing sin riesgo de truncamiento y añade una barrera básica contra valores claramente inválidos.

---

## ADR-006 · Valores categóricos – Migración de CHECK a tablas catálogo

**Problema identificado**
Campos como `transaction_type` usaban restricciones `CHECK` para limitar sus valores posibles. Este enfoque se vuelve difícil de mantener cuando los valores cambian, se reutilizan en otras tablas o necesitan administrarse desde la aplicación.

**Solución adoptada**
Se crean tablas catálogo independientes para estos valores. Las tablas que los usan los referencian mediante claves foráneas.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Mantener `CHECK` | Poco flexible ante cambios y difícil de reutilizar |
| Usar `ENUM` nativo de PostgreSQL | Costoso de modificar en migraciones futuras |

**Impacto esperado**
Los valores categóricos pueden crecer y administrarse sin modificar el esquema estructural. El modelo gana extensibilidad a cambio de algunas tablas adicionales.

---

## ADR-007 · updated_at – Mantenimiento automático vía trigger

**Problema identificado**
El campo `updated_at` dependía de que la aplicación lo actualizara explícitamente en cada operación de escritura. Cualquier omisión en el backend dejaba el campo desactualizado, afectando la trazabilidad de los registros.

**Solución adoptada**
Se implementan triggers que actualizan `updated_at` automáticamente en cada operación `UPDATE`, sin depender de la capa de aplicación.

**Opciones descartadas**

| Opción | Razón para descartar |
|---|---|
| Actualizar desde el backend | Propenso a omisiones y difícil de auditar |
| Eliminar el campo | Se pierde trazabilidad básica de cambios |

**Impacto esperado**
La auditoría de cambios se vuelve confiable por diseño. El costo es un overhead menor por trigger en cada `UPDATE`.

---

## Visión general de los cambios

Estas siete decisiones tienen un hilo conductor común: **llevar las reglas de negocio críticas al nivel del esquema**, reduciendo la dependencia de la aplicación para garantizar la integridad y consistencia de los datos.

Los cambios tocan cuatro ejes principales:

1. **Integridad referencial** — ADR-001, ADR-002, ADR-003
2. **Calidad y unicidad de datos** — ADR-004, ADR-005
3. **Extensibilidad del modelo** — ADR-006
4. **Trazabilidad automática** — ADR-007