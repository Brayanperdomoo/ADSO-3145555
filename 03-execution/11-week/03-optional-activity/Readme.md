🏋️‍♂️ Sistema de Gestión de Gimnasio
📌 Descripción del Proyecto

Este proyecto corresponde al desarrollo de un sistema integral de gestión para un gimnasio, el cual permite administrar usuarios, servicios, membresías y operaciones internas mediante una arquitectura moderna basada en frontend, backend y base de datos.

El sistema está diseñado bajo principios de separación de responsabilidades, escalabilidad y buenas prácticas de desarrollo, permitiendo una solución robusta y mantenible.

🎯 Objetivo

Desarrollar una plataforma que permita:

Gestionar usuarios del gimnasio
Administrar membresías y servicios
Controlar operaciones internas
Implementar una arquitectura cliente-servidor
Garantizar persistencia de datos estructurada
🧩 Arquitectura del Proyecto

El sistema está dividido en múltiples repositorios para garantizar organización y escalabilidad:

🔹 1. Base de Datos

📂 Repositorio:
👉 https://github.com/Brayanperdomoo/Gimnasio-DB.git

Contiene:

Modelo relacional del sistema
Scripts SQL
Estructura de tablas
Relaciones y llaves foráneas
🔹 2. Backend (API)

📂 Repositorio:
👉 https://github.com/Brayanperdomoo/Gimnasio-Backend.git

Encargado de:

Lógica de negocio
Exposición de API REST
Validaciones
Conexión con base de datos
🔹 3. Frontend

📂 Repositorio:
👉 https://github.com/Brayanperdomoo/Gimnasio-Frontend.git

Interfaz de usuario que permite:

Visualización de datos
Interacción con el sistema
Consumo de la API
Experiencia amigable para el usuario
🔹 4. Repositorio Principal (Integración)

📂 Repositorio:
👉 https://github.com/Brayanperdomoo/Gimnasio-Trabajo.git

Este repositorio contiene:

Integración de los módulos
Organización general del proyecto
Documentación adicional
Estructura global del sistema

🔄 Flujo del Sistema
El usuario interactúa con el Frontend
El frontend realiza solicitudes al Backend (API)
El backend procesa la lógica y consulta la Base de Datos
Se retorna la información al frontend
El usuario visualiza los resultados

📊 Características del Sistema
✔️ Arquitectura en capas
✔️ Separación de responsabilidades
✔️ Escalable y mantenible
✔️ Interfaz amigable
✔️ Gestión eficiente de datos