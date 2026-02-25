1. Problema
Actualmente, la cafetería del SENA atiende a los usuarios de manera manual, lo que genera filas largas y hace que los tiempos de descanso se reduzcan. Esto causa molestias tanto para los estudiantes como para el personal del cafetín.
________________________________________
2. Objetivo general
Crear un sistema de carrito de compras que ayude a organizar mejor los pedidos de la cafetería del SENA, permitiendo una atención más rápida y eficiente.
________________________________________
3. Objetivos específicos
•	Mostrar a los usuarios los productos disponibles de forma clara y rápida.
•	Permitir que los usuarios hagan sus pedidos fácilmente.
•	Ayudar al personal del cafetín a organizar y atender los pedidos sin errores.
•	Disminuir el tiempo de espera y las confusiones al tomar pedidos.
________________________________________
4. Alcance del proyecto
El sistema permitirá:
•	Ver los productos disponibles en el cafetín.
•	Agregar productos a un carrito de compras.
•	Confirmar pedidos por parte del usuario.
•	Que el personal del cafetín vea y gestione los pedidos.
•	Cambiar el estado de los pedidos.
El sistema no incluirá:
•	Pagos en línea.
•	Facturación electrónica.
•	Notificaciones avanzadas.
•	Conexión con otros sistemas.
________________________________________
5. Requerimientos
5.1 Requerimientos funcionales
•	El sistema debe mostrar los productos del cafetín.
•	Debe permitir agregar productos al carrito.
•	Debe permitir confirmar un pedido.
•	El personal del cafetín debe poder ver los pedidos.
•	Se debe poder cambiar el estado de cada pedido.
5.2 Requerimientos no funcionales
•	El sistema debe ser fácil de usar, sin necesidad de explicación previa.
•	Debe responder rápido para no generar demoras.
•	Debe estar disponible durante el horario de atención del cafetín.
•	Debe funcionar tanto en computadores como en celulares.
•	Los pedidos deben guardarse correctamente sin perder información.
5.3 Reglas del negocio
•	Un pedido confirmado no se puede modificar.
•	Si un producto no está disponible, no se puede seleccionar.
•	Todo pedido debe tener un estado (pendiente, en proceso, listo, etc.).
________________________________________
6. Priorización MoSCoW
•	Must (obligatorio): Ver productos, carrito de compras, confirmar pedidos, ver pedidos.
•	Should (importante): Cambiar el estado del pedido.
•	Could (opcional): Historial de pedidos.
•	Won’t (no por ahora): Pagos en línea.
________________________________________
7. Mockup inicial
El prototipo del sistema incluye:
•	Pantalla de productos.
•	Pantalla del carrito de compras.
•	Pantalla para confirmar el pedido.
•	Pantalla para que el cafetín gestione los pedidos.
Link del diseño:
https://stitch.withgoogle.com/projects/3664736573573246328

________________________________________
8. Backlog / Plan de trabajo
Historias de usuario:
•	Como usuario, quiero ver los productos para escoger mi pedido.
•	Como usuario, quiero confirmar mi pedido para ahorrar tiempo.
•	Como personal del cafetín, quiero ver los pedidos para organizarlos mejor.
Todas las historias tienen prioridad alta.
________________________________________
9. Modelo de datos
Entidades principales:
•	Usuario: id_usuario, nombre, correo.
•	Producto: id_producto, nombre, precio, disponibilidad.
•	Pedido: id_pedido, fecha, estado, id_usuario.
•	DetallePedido: id_detalle, cantidad, id_pedido, id_producto.
Relaciones:
•	Un usuario puede hacer varios pedidos.
•	Cada pedido pertenece a un usuario.
•	Un pedido puede tener varios productos.
•	La relación entre pedidos y productos se maneja con DetallePedido.
_________________________________________________________________________________

Link de trabajo en grupo de Scrum, Design Thinking y MoSCoW. 

https://gamma.app/docs/Scrum-Design-Thinking-y-MoSCoW-03yx9kud5ixbhik

_________________________________________________________________________________
Link de trabajo en grupo de De Design Thinking + MoSCoW + Scrum al MVP
Carrito de compras — Cafetín SENA (Industria)

https://gamma.app/docs/De-Design-Thinking-MoSCoW-Scrum-al-MVP-9kgxi56kzarrn12








