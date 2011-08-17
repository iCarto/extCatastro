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
	    te.addGeometryWithParametrizedValues(feature.getGeometry()
		    .cloneGeometry(), getNewPredioValues(feature),
		    "_create_new_predio");
	    te.stopEditing(destinationLayer, false);
	    destinationLayer.setActive(false);
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

    private Value[] getNewPredioValues(IFeature feature) {
	Value[] predioValues = new Value[11];
	Value[] featureValues = feature.getAttributes().clone();
	// int destinationNumFields;
	// int sourceNumFields;
	// try {
	// sourceNumFields = sourceLayer.getRecordset().getFieldCount();
	// destinationNumFields =
	// destinationLayer.getRecordset().getFieldCount();
	// Value[] prediovalues = new Value[destinationNumFields];
	// for(int i = 0; i < destinationNumFields; i++) {
	//		
	// }
	for (int i = 0; i < featureValues.length; i++) {
	    predioValues[0] = featureValues[0]; // pais_cve
	    predioValues[1] = featureValues[1]; // edo_cve
	    predioValues[2] = featureValues[2]; // mun_cve
	    predioValues[3] = featureValues[3]; // reg_cve
	    predioValues[4] = featureValues[4]; // lim_cve
	    predioValues[5] = featureValues[6]; // man_cve
	    predioValues[6] = ValueFactory.createValue(1); // catmetadato_id
	    predioValues[7] = ValueFactory.createValue(1); // pre_cve
	    predioValues[8] = featureValues[7]; // pre_area
	    predioValues[9] = ValueFactory.createNullValue(); // zon_id
	    predioValues[10] = ValueFactory.createValue(""); // gid
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
