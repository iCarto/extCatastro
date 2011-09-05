Resumen
*******

* Las extensiones de selección, inicializan la herramienta de selección por punto de gvSIG.
* Un listener sobre las acciones de la anterior herramienta se encarga de inicializar la información necesaria sobre el área seleccionada.
* Las estructuras ``ConstantManager.java`` y ``Constants.java`` mantienen y permiten consultar la información en cualquier punto del programa.
* El usuario puede ver la información de constantes seleccionadas en la statusbar de gvSIG.

Paquetes java relacionados:

* ``es.icarto.gvsig.catastro``: extensiones de selección.
* ``es.icarto.gvsig.catastro.constants``: estructuras para mantener la información y rellenarla.
* ``config.xml``: para incluir la statusbar de las constantes.

Flujo de la selección
*********************

Las extensiones de selección, permiten al usuario seleccionar el área sobre la
que van a trabajar, así como mantener esa información a lo largo de la
aplicación, para que otras extensiones puedan consultarla (lo que llamaremos
constantes de operación).

Para realizar lo anterior, cada extensión usará la información que provee la
herramienta de selección por punto de gvsig.

En código, esto se convierte en un listener sobre las acciones de esa
herramienta. Así, para tomar la información, cada una de las extensiones
mantiene el listener ConstantsSelectionListener, que se encargará de inicializar
las estructuras ``Constants.java`` y ``ConstantManager.java``.

Cada una de las extensiones, en su método execute, tendrá pues un código como el siguiente:

::

  View view = (View) PluginServices.getMDIManager().getActiveWindow();
  MapControl mc = view.getMapControl();
  ConstantsSelectionListener csl = new ConstantsSelectionListener(mc);
  mc.addMapTool("constantsSelectionManzana", new PointBehavior(csl));
  mc.setTool("constantsSelectionManzana");

Desde este momento, el usuario tiene a su disposición la herramienta de
selección por punto de gvSIG. Una vez pinche sobre una entidad, el listener que
se ha inicializado se encargará de setear las constantes necesarias.

El listener de selección
************************

La clase ``ConstantsSelectionListener.java`` hereda de
``PointSelectionListener.java`` e implementa su método callback, que será
llamado una vez el usuario seleccione una entidad.

El código del núcleo de esta clase es:

::

   @Override
   public void point(PointEvent event) throws BehaviorException {
       switch (layer) {
           case LAYER_IS_PREDIO:
                // set constants for predio
                break;
           case LAYER_IS_MANZANA:
                //set constants for manzana
                break;
           case LAYER_IS_REGION:
                // set constants for region
                break;
   }

La herramienta de selección por punto de gvSIG depende de la capa activa. Es por
ello que las comprobaciones sobre qué constantes deben inicializarse se realizan
mediante la comprobación de la capa activa.

Las constantes
**************

Las constantes de selección se mantienen mediante la estructura
``Constants.java``. En esta clase se mantienen información sobre la región,
manzana y/o predio seleccionados.

Esta estructura también proporciona acceso a otras variables que pueden ser
consideradas constantes pero en este momento son fijas para toda la operación
del programa: país, estado, municipio y límite municipal. Estas últimas son
ahora definidas como variables estáticas en la clase `Preferences.java``, pero
se han incluido en la estructura de constantes por coherencia. El desarrollador
que use esta estructura necesita la información conjunta sin preocuparse del
lugar donde ésta sea almacenada.

::

 public String getPais() {
    //TODO: este campo se debe recuperar de una tabla de la BD
    return Preferences.PAIS;
 }

A lo largo de la ejecución del programa, cuando sea necesario leer el valor de
 las constantes, se utilizará la estructura ``ConstantManager.java``. Esta clase
 mantiene una variable de clase llamada ``constants`` (que es compartida por todos las instancias de la clase).

 private static Constants constants = null;

Así, aunque ``ConstantManager.java`` no es un singleton en sí mismo, se comporta
como tal, pues devuelve la misma variable constants a cualquiera de sus
instancias o la crea si no existe:

::

 public Constants getConstants(){
    if(constants == null){
        constants = new Constants();
            return constants;
            }
            return constants;
 }

Las constantes en el statusbar
******************************

Por otro lado, el listener de selección por punto, se encarga también de
actualizar la información existente en la barra de estado de gvSIG. El método
que lo hace es el siguiente:

::

 private void addConstantsToStatusBar() {
     MDIFrame mF = (MDIFrame) PluginServices.getMainFrame();
     NewStatusBar footerStatusBar = mF.getStatusBar();
     Constants constants = constantManager.getConstants();
     String constantsInfo = getConstantsInfo(constants);
     footerStatusBar.setMessage("constants", constantsInfo);
 }

La creación de la barra se realiza en el archivo ``config.xml`` con el siguiente
código:

::

 <label-set class-name="com.iver.cit.gvsig.project.documents.view.gui.View">
     <label id="units" size="75"/>
     <label id="x" size="120"/>
     <label id="y" size="120"/>
     <label id="4" size="110"/>
     <label id="5" size="110"/>
     <label id="distancearea" size="30"/>
     <label id="projection" size="110"/>
     <label id="constants" size="140"/> <!-- cuadro de constantes -->
 </label-set>

Notar que durante las etapas de desarrollo ha bastado con incluir este código en
el archivo config.xml, mientras que en producción, ha sido necesario incluir la
penúltima línea ``<label id="constants" size="140"/> <!-- cuadro de constantes
-->`` en el propio ``config.xml`` de la extensión appgvSIG, que es la encargada
final de crear y mantener los cuadros en la barra de estado.

