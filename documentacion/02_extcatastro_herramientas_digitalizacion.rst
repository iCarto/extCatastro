Resumen
=======

* Las herramientas de digitalización son las contenidas en el proyecto
  OpenCADTools, una extensión para gvSIG.
* Cada acción en el menú de la extensión extCatastro llama a una de las
  herramientas de digitalización de las openCADTools.
* Las herramientas de openCADTools proveen de listeners de fin de edición al desarrollador para que pueda procesar la información necesaria al finalizar.

Paquetes java relacionados:

* ``es.icarto.gvsig.catastro``: extensiones de digitalización que inicializan las CADTools.
* ``es.icarto.gvsig.catastro.wrapperscadtools``: Wrappers para inicializar las CADTools.

En detalle
==========

Se muestran a continuación las herramientas CAD inicializadas por cada una de
las extensiones del menú de extCatastro:

* ConstruccionPolygonExtension -> CutPolygonCADTool
* ConstruccionFusionExtension -> SelectionGeometryCADTool
* ConstruccionNewExtension -> InsertAreaCADTool
* ManzanaNewExtension -> InsertAreaCADTool
* PredioDivideExtension -> CutPolygonCADTool
* PredioFusionExtension -> SelectionGeometryCADTool

La operación con cada una de las herramientas puede consultarse en el manual de
usuario de las OpenCADTools adjunto.

A nivel implementación, debido a que el lenguaje Java no permite múltiple
herencia, se ha hecho lo siguiente:

* Por cada acción del menú, crear una extensión, que hereda de la clase
  Extension, de gvSIG.
* Cada una de las extensiones mantiene un wrapper sobre la CADTool que
  necesita. Estos wrappers se pueden encontrar en el paquete
  ``es.icarto.gvsig.catastro.wrapperscadtools``.
* Cada uno de los wrappers, hereda de la extensión que lanza la CADTool (se
  pueden ver en el proyecto extCAD) y delega la lógica de aplicación en los
  métodos propios de la extensión padre.

