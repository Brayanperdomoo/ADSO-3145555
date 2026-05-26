# Hotel Management Repo

Repositorio: [Brayanperdomoo/hotel-management-repo](https://github.com/Brayanperdomoo/hotel-management-repo)

Repositorio orientado a la **gestión y estructuración de la base de datos** de un sistema hotelero. Incluye scripts organizados por capas de trabajo para facilitar el mantenimiento, la trazabilidad y la automatización del despliegue.

## Descripción

Este proyecto centraliza la definición de la base de datos del sistema hotelero mediante una organización modular de scripts SQL. La estructura permite separar claramente la creación de objetos, la carga de datos, la seguridad, el control transaccional y los rollback, lo que mejora el orden del proyecto y simplifica futuras modificaciones.

## Objetivo

Diseñar una base de datos robusta, ordenada y fácil de mantener para soportar un sistema de administración hotelera.

## Estructura del repositorio

```bash
hotel-management-repo/
├── .github/
│   └── workflows/
│       └── ci.yml
├── database/
│   ├── 01_ddl/
│   │   ├── 00_extensions/
│   │   ├── 01_schemas/
│   │   ├── 02_types/
│   │   ├── 03_tables/
│   │   ├── 04_views/
│   │   ├── 05_materialized_views/
│   │   ├── 06_functions/
│   │   ├── 07_procedures/
│   │   ├── 08_triggers/
│   │   └── 09_indexes/
│   ├── 02_dml/
│   │   ├── 00_inserts/
│   │   ├── 01_updates/
│   │   ├── 02_deletes/
│   │   ├── 03_upserts/
│   │   └── 04_patches/
│   ├── 03_dcl/
│   │   ├── 00_roles/
│   │   ├── 01_grants/
│   │   └── 02_policies/
│   ├── 04_tcl/
│   │   ├── 00_transaction_blocks/
│   │   └── 01_manual_recoveries/
│   ├── 05_rollbacks/
│   ├── changelog-master.yaml
│   ├── liquibase.properties
│   └── docker-compose.yml
└── README.md
```

## Contenido principal

### 1. DDL (Data Definition Language)

Scripts para definir la estructura de la base de datos:

* extensiones
* esquemas
* tipos de datos personalizados
* tablas
* vistas y vistas materializadas
* funciones y procedimientos
* triggers
* índices

### 2. DML (Data Manipulation Language)

Scripts para administrar la información almacenada:

* inserciones
* actualizaciones
* eliminaciones
* operaciones upsert
* parches de datos

### 3. DCL (Data Control Language)

Scripts orientados a la seguridad y control de acceso:

* roles
* permisos (grants)
* políticas

### 4. TCL (Transaction Control Language)

Scripts para control de transacciones y recuperación manual:

* bloques transaccionales
* recuperaciones manuales

### 5. Rollbacks

Conjunto de scripts para revertir cambios aplicados en la base de datos cuando sea necesario.

## Tecnologías usadas

* **PostgreSQL**
* **SQL / PLpgSQL**
* **Liquibase**
* **Docker**
* **GitHub Actions**

## Uso

1. Clona el repositorio:

   ```bash
   git clone https://github.com/Brayanperdomoo/hotel-management-repo.git
   ```
2. Configura la conexión a la base de datos en `liquibase.properties`.
3. Revisa `changelog-master.yaml` para verificar el orden de ejecución.
4. Ejecuta los scripts según el módulo que necesites.

## Convenciones del proyecto

* Mantener los scripts separados por tipo de operación.
* Usar nombres claros y descriptivos.
* Respetar el orden definido en el changelog maestro.
* Incluir rollback cuando un cambio lo requiera.
* Documentar cualquier ajuste importante en el repositorio.

## Integración continua

El repositorio incluye un flujo de trabajo en `.github/workflows/ci.yml` para apoyar la validación automática del proyecto.

## Estado del proyecto

Proyecto en desarrollo, enfocado en la consolidación de la base de datos del sistema hotelero.

## Autor

**Brayan Perdomo**

## Licencia

Este proyecto no tiene una licencia definida actualmente.
