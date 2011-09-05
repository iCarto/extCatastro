Las siguientes estructuras facilitan el trabajo con gvSIG, creando wrappers
sobre acciones comunes de la plataforma para que sean más sencillas de utilizar
para el desarrollador.

TOCLayerManager.java
====================

Se encarga de mantener el estado de las capas en el TOC (Table of Contents) y
provee funciones para consultar su estado y/o cambiarlo.

ToggleEditing.java
==================

Se encarga de la gestión de edición de capas. Contiene métodos para poner en
edición la capa, cerrarla y modificar sus contenidos.

Es importante resaltar que, el proceso de edición debe ser tal que así:

::

 ToggleEditing te = new ToggleEditing();
 te.startEditing(layer);
 te.modifyValues();
 te.stopEditing(layer, saveOrNotBoolean);

Es decir, el proceso de edición requiere poner la capa en edición previamente a
poder modificar sus valores y finalmente cerrar la capa, que es el momento en
que esos valores se guardarán o no, dependiendo del valor del booleano que se le indique.

Preferences.java
================

Esta clase contiene variables estáticas del sistema. Algunas de ellas deberían
residir en base de datos, de cara a facilitar nuevas instalaciones del sistema
en otros entornos.


