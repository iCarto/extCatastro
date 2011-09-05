Las funcionalidades implementadas para la extensión extCatastro, contienen en
términos generales los siguientes desarrollos:

* Selección del área de trabajo
* Herramientas de digitalización
* Implementación de reglas de negocio y topológicas


Para un usuario, el punto de entrada en la extensión son las distintas acciones
de los menús. Para el desarrollador, cada una de ellas puede ser vista como una
extensión, que se encontrará en el paquete java llamado ``es.icarto.gvsig.catastro``.

Como anteriormente se apuntaba, en este paquete se encuentran:

* Extensiones de selección:

  * Selección de la región: clase ``SelectionRegionExtension.java``
  * Selección de la manzana: clase ``SelectionManzanaExtension.java``
  * Selección del predio: clase ``SelectionPredioExtension.java``
  * Resetear la sección: clase ``SelectionResetConstantsExtension.java``

* Extensiones de digitalización:

  * Dividir una construcción: clase ``ConstruccionDivideExtension.java``
  * Fusionar una construcción: clase ``ConstruccionFusionExtension.java``
  * Crear una nueva construcción: clase ``ConstruccionNewExtension.java``
  * Crear una nueva manzana: clase ``ManzanaNewExtension.java``
  * Dividir un predio: ``PredioDivideExtension.java``
  * Fusionar un predio: ``PredioFusionExtension.java``

* Extensiones de las reglas:

  * Lanzar las comprobaciones y acciones topológicas y de negocio: clase ``ActionDispatcherExtension.java``

A lo largo de las siguientes secciones se explicará en detalle cada una de las anteriores funcionalidades.
