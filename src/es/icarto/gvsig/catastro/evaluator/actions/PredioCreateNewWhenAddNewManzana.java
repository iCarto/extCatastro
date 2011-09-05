package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class PredioCreateNewWhenAddNewManzana implements IAction {

    private static FLyrVect sourceLayer;
    private FLyrVect destinationLayer;
    private final TOCLayerManager tocLayerManager;

    public PredioCreateNewWhenAddNewManzana(FLyrVect layer) {
	tocLayerManager = new TOCLayerManager();
	this.sourceLayer = layer;
	destinationLayer = getDestinationLayer();
    }

    public boolean execute() {
	try {
	    ToggleEditing te = new ToggleEditing();
	    boolean wasEditingManzanas = false;
	    if (tocLayerManager.isManzanaLayerInEdition()) {
		te.stopEditing(sourceLayer, false);
		sourceLayer.setActive(false);
		wasEditingManzanas = true;
	    }
	    destinationLayer.setActive(true);
	    te.startEditing(destinationLayer);
	    FBitSet indexes = sourceLayer.getRecordset().getSelection();
	    IFeature feature = sourceLayer.getSource().getFeature(
		    indexes.nextSetBit(0));
	    // TODO: check ID
	    te.addGeometryWithParametrizedValues(feature.getGeometry()
		    .cloneGeometry(), getNewPredioValues(feature),
		    "_create_new_predio");
	    if (wasEditingManzanas) {
		te.startEditing(sourceLayer);
		sourceLayer.setActive(true);
	    }
	    return true;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private Value[] getNewPredioValues(IFeature manzanaFeature) {
	Value[] predioValues = new Value[11];
	Value[] manzanaValues = manzanaFeature.getAttributes().clone();

	try {
	    int paisIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.PAIS_NAME_IN_DB);
	    int estadoIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.ESTADO_NAME_IN_DB);
	    int municipioIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.MUNICIPIO_NAME_IN_DB);
	    int regionIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.REGION_NAME_IN_DB);
	    int limiteIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.LIMITE_NAME_IN_DB);
	    int manzanaIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.MANZANA_NAME_IN_DB);
	    int catmetadatoIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.CATMETADATO_IN_DB);
	    int predioIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.PREDIO_NAME_IN_DB);
	    int predioAreaIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.PREDIO_AREA_NAME_IN_DB);
	    int zonIndexInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.ZONA_NAME_IN_DB);
	    int gidInPredio = destinationLayer.getRecordset()
		    .getFieldIndexByName(Preferences.GID_IN_DB);

	    int paisIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.PAIS_NAME_IN_DB);
	    int estadoIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.ESTADO_NAME_IN_DB);
	    int municipioIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.MUNICIPIO_NAME_IN_DB);
	    int regionIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.REGION_NAME_IN_DB);
	    int limiteIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.LIMITE_NAME_IN_DB);
	    int manzanaIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.MANZANA_NAME_IN_DB);
	    int manzanaAreaIndexInManzana = sourceLayer.getRecordset()
		    .getFieldIndexByName(Preferences.MANZANA_AREA_NAME_IN_DB);

	    for (int i = 0; i < manzanaValues.length; i++) {
		predioValues[paisIndexInPredio] = manzanaValues[paisIndexInManzana]; // pais_cve
		predioValues[estadoIndexInPredio] = manzanaValues[estadoIndexInManzana]; // edo_cve
		predioValues[municipioIndexInPredio] = manzanaValues[municipioIndexInManzana]; // mun_cve
		predioValues[regionIndexInPredio] = manzanaValues[regionIndexInManzana]; // reg_cve
		predioValues[limiteIndexInPredio] = manzanaValues[limiteIndexInManzana]; // lim_cve
		predioValues[manzanaIndexInPredio] = manzanaValues[manzanaIndexInManzana]; // man_cve
		predioValues[catmetadatoIndexInPredio] = ValueFactory
			.createValue(1); // catmetadato_id
		predioValues[predioIndexInPredio] = ValueFactory.createValue(1); // pre_cve
		// pre_area = man_area
		predioValues[predioAreaIndexInPredio] = manzanaValues[manzanaAreaIndexInManzana];
		predioValues[zonIndexInPredio] = ValueFactory.createNullValue(); // zon_id
		predioValues[gidInPredio] = ValueFactory.createNullValue(); // gid
	    }

	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}

	return predioValues;
    }

    private FLyrVect getDestinationLayer() {
	destinationLayer = tocLayerManager
		.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
	return destinationLayer;
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_create_predio_was_not_possible");
    }
}
