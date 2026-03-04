# Diagramas BPMN 2.0 - Sistema Carrito de Compras Cafetín SENA

## 📋 Descripción General

Se han creado dos diagramas BPMN 2.0 siguiendo la notación estándar compatible con Camunda y herramientas BPMN estándar:

1. **BPMN Técnico** (`BPMN_Tecnico_Camunda.bpmn`) - 35 KB
2. **BPMN Formal** (`BPMN_Formal_Camunda.bpmn`) - 18 KB

## 🎯 Características de los Diagramas

### Elementos BPMN 2.0 Utilizados

#### ✅ Pools y Lanes (Piscinas y Carriles)
- **Pools**: Representan participantes principales del proceso
  - Cliente
  - Sistema Frontend / Sistema de Pedidos
  - API Backend / Personal del Cafetín
  
- **Lanes**: Subdividen las responsabilidades dentro de cada pool
  - Ejemplo: "Gestión de Catálogo" vs "Procesamiento de Pedido"

#### ✅ Eventos
- **Start Events** (Círculo simple): Inicio del proceso
  - `startEvent`: Inicio estándar
  - `messageEventDefinition`: Inicio por mensaje
  
- **End Events** (Círculo con borde grueso): Fin del proceso
  - `endEvent`: Fin estándar
  - `terminateEventDefinition`: Fin con terminación
  - `messageEventDefinition`: Fin con mensaje
  - `errorEventDefinition`: Fin con error
  
- **Intermediate Events** (Círculo con doble borde): Eventos intermedios
  - `intermediateCatchEvent`: Espera de mensaje/evento

#### ✅ Actividades
- **User Tasks** (Rectángulo con icono de persona): Tareas manuales del usuario
  - `userTask`: Acciones del cliente o personal
  
- **Service Tasks** (Rectángulo con icono de engrane): Tareas automáticas del sistema
  - `serviceTask`: Llamadas a APIs, servicios
  - Atributos Camunda: `camunda:type`, `camunda:topic`
  
- **Script Tasks** (Rectángulo con icono de script): Ejecución de código
  - `scriptTask`: Cálculos, transformaciones
  
- **Business Rule Tasks** (Rectángulo con icono de tabla): Reglas de negocio
  - `businessRuleTask`: Validaciones con reglas

#### ✅ Gateways (Compuertas)
- **Exclusive Gateway** (Rombo con X): Decisiones excluyentes
  - `exclusiveGateway`: Solo un camino se ejecuta
  - Ejemplo: ¿Confirmar pedido? → Sí / No
  
- **Parallel Gateway** (Rombo con +): Ejecución paralela
  - `parallelGateway`: Múltiples caminos simultáneos
  - Usado en: Inserción simultánea de datos en transacción

#### ✅ Conectores
- **Sequence Flow** (Flecha continua): Flujo dentro de un pool
  - `sequenceFlow`: Orden de ejecución de actividades
  
- **Message Flow** (Flecha punteada): Comunicación entre pools
  - `messageFlow`: Intercambio de mensajes entre participantes

#### ✅ Objetos de Datos
- **Data Objects**: Datos manipulados en el proceso
  - `dataObjectReference`: Carrito de compras, producto
  
- **Data Stores**: Almacenamiento persistente
  - `dataStoreReference`: PostgreSQL, sessionStorage

#### ✅ Manejo de Errores
- **Boundary Events**: Eventos adjuntos a actividades
  - `boundaryEvent`: Captura errores en transacciones
  - `errorEventDefinition`: Manejo de excepciones

---

## 📊 BPMN Técnico - Características

### Orientado a: Desarrolladores y Arquitectos

### Pools:
1. **Cliente** - Interacción del usuario
2. **Sistema Frontend** - Aplicación web (HTML5, CSS3, JavaScript)
3. **API Backend** - Servicios REST (Go + PostgreSQL)

### Detalles Técnicos Incluidos:
- ✅ Endpoints REST completos
  - `GET /api/productos`
  - `POST /api/carrito`
  - `PUT /api/carrito/{id}`
  - `DELETE /api/carrito/{id}`
  - `POST /api/pedidos`
  
- ✅ Queries SQL documentados
  ```sql
  SELECT * FROM productos WHERE disponible = true AND stock > 0
  INSERT INTO carrito_temporal (session_id, producto_id, cantidad, precio_unitario)
  UPDATE productos SET stock = stock - ?
  DELETE FROM carrito_temporal WHERE session_id = ?
  ```

- ✅ Códigos de respuesta HTTP
  - 200 OK
  - 201 Created
  - 204 No Content
  - 409 Conflict
  - 500 Internal Server Error

- ✅ Manejo de transacciones ACID
  - BEGIN TRANSACTION
  - Operaciones paralelas (INSERT pedidos, detalle, UPDATE stock)
  - COMMIT / ROLLBACK

- ✅ Validaciones y reglas de negocio
  - RN1: Disponibilidad de productos
  - RN2: Cantidad mínima = 1
  - RN3: Confirmación obligatoria

- ✅ Seguridad
  - RNF2: HTTPS, cifrado en tránsito
  - Validaciones backend

- ✅ Performance
  - RNF3: Tiempo de respuesta < 3 segundos

### Tecnologías Referenciadas:
- Frontend: HTML5, CSS3, JavaScript, sessionStorage
- Backend: Go (Golang), Gin Framework
- Base de datos: PostgreSQL
- Protocolo: REST API (JSON)

---

## 📈 BPMN Formal - Características

### Orientado a: Stakeholders, Gerencia, Usuarios de Negocio

### Pools:
1. **Cliente** - Usuario del sistema
2. **Sistema de Pedidos** - Aplicación
3. **Personal del Cafetín** - Empleados

### Lanes por Pool:

**Cliente:**
- Solicitud y Selección
- Confirmación y Retiro

**Sistema:**
- Gestión de Catálogo y Carrito
- Procesamiento de Pedido

**Personal:**
- Preparación
- Entrega

### Enfoque en:
- ✅ Proceso end-to-end completo
- ✅ Interacciones entre actores
- ✅ Flujo desde pedido hasta entrega
- ✅ Reglas de negocio en lenguaje natural
- ✅ Objetivos del negocio

### Sin Detalles Técnicos:
- ❌ No incluye endpoints
- ❌ No incluye queries SQL
- ❌ No incluye códigos HTTP
- ❌ Enfoque en el "qué", no en el "cómo"

---

## 🛠️ Herramientas para Visualizar

### Opción 1: Camunda Modeler (Recomendado)
```bash
# Descargar desde: https://camunda.com/download/modeler/
# Gratis y de código abierto
# Soporta BPMN 2.0 completo con todas las extensiones
```

### Opción 2: bpmn.io
```bash
# Web: https://demo.bpmn.io/
# Simplemente arrastra el archivo .bpmn
```

### Opción 3: Visual Studio Code
```bash
# Extensión: BPMN Viewer
# Instalación:
code --install-extension hediet.vscode-drawio
```

### Opción 4: Importar a Camunda Platform
```bash
# Para ejecutar procesos reales
# Requiere Camunda Platform 7 o 8
```

---

## 📝 Requerimientos Cubiertos

### Requerimientos Funcionales:
- ✅ RF1: Ver productos
- ✅ RF2: Agregar al carrito
- ✅ RF3: Modificar cantidad
- ✅ RF4: Calcular total
- ✅ RF5: Ver pedido

### Reglas de Negocio:
- ✅ RN1: Disponibilidad de productos
- ✅ RN2: Cantidad mínima = 1 unidad
- ✅ RN3: Confirmación obligatoria

### Requerimientos No Funcionales:
- ✅ RNF1: Interfaz intuitiva
- ✅ RNF2: Datos seguros
- ✅ RNF3: Respuesta < 3 segundos

---

## 🔄 Diferencias Clave Entre Ambos Diagramas

| Aspecto | BPMN Técnico | BPMN Formal |
|---------|--------------|-------------|
| **Audiencia** | Desarrolladores, arquitectos | Gerencia, stakeholders |
| **Detalle** | Muy alto (SQL, APIs, HTTP) | Alto nivel (proceso negocio) |
| **Tamaño** | 35 KB | 18 KB |
| **Pools** | Cliente, Frontend, Backend | Cliente, Sistema, Personal |
| **Lenguaje** | Técnico (GET, POST, INSERT) | Negocio (Presentar, Registrar) |
| **Objetivo** | Implementación del sistema | Comprensión del proceso |
| **Ejecutable** | Sí (con adaptaciones) | No (documentación) |

---

## 🎓 Elementos BPMN 2.0 Estándar Utilizados

### Notación Completa:

```
Eventos:
  ○       Start Event (inicio)
  ◎       Intermediate Event (intermedio)
  ◉       End Event (fin)
  ✉       Message (mensaje)
  ⚠       Error (error)
  ⊗       Terminate (terminación)

Actividades:
  ┌─────┐
  │     │ Task (tarea genérica)
  └─────┘
  
  ┌─────┐
  │ 👤  │ User Task (tarea manual)
  └─────┘
  
  ┌─────┐
  │ ⚙   │ Service Task (tarea automática)
  └─────┘
  
  ┌─────┐
  │ 📜  │ Script Task (script)
  └─────┘
  
  ┌─────┐
  │ 📋  │ Business Rule Task (regla)
  └─────┘

Gateways:
  ◇   Data-based Exclusive Gateway (XOR)
  ◈   Parallel Gateway (AND)
  ◊   Event-based Gateway
  
Conectores:
  ──→  Sequence Flow (flujo secuencial)
  ··→  Message Flow (flujo de mensaje)
  ━━━  Association (asociación)

Datos:
  📄   Data Object (objeto de datos)
  🗄️   Data Store (almacén de datos)
```

---

## 📚 Referencias y Estándares

- **BPMN 2.0 Specification**: https://www.omg.org/spec/BPMN/2.0/
- **Camunda BPMN Symbols**: https://camunda.com/bpmn/reference/
- **BPMN Best Practices**: https://camunda.com/best-practices/

---

## ✅ Validación de Archivos

Ambos archivos son XML válido que cumple con:
- ✅ BPMN 2.0 Schema (xmlns:bpmn)
- ✅ BPMN DI Schema (xmlns:bpmndi) - para diagramas
- ✅ Extensiones Camunda (xmlns:camunda) - solo en técnico
- ✅ Namespaces correctos
- ✅ Estructura de proceso ejecutable

---

## 🚀 Siguiente Paso Recomendado

1. Abrir archivos en **Camunda Modeler**
2. Verificar visualización correcta
3. Ajustar posicionamiento si es necesario (los archivos tienen estructura, no layout visual)
4. Exportar a PNG/SVG para documentación
5. Importar a Camunda Platform para ejecución (solo técnico)

---

**Autor**: Claude Sonnet 4.5  
**Fecha**: Marzo 2026  
**Proyecto**: Sistema Carrito de Compras Cafetín SENA  
**Versión BPMN**: 2.0  
**Compatibilidad**: Camunda 7.x, 8.x, bpmn.io
