package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class CreatePredioWhenAddNewManzana implements IAction {

    private static FLyrVect sourceLayer;
    private FLyrVect destinationLayer;
    private final TOCLayerManager tocLayerManager;

    public CreatePredioWhenAddNewManzana(FLyrVect layer) {
	tocLayerManager = new TOCLayerManager();
	this.sourceLayer = layer;
	destinationLayer = getDestinationLayer();
    }

    public boolean execute() {
	try {
	    ToggleEditing te = new ToggleEditing();
	    boolean wasEditingManzanas = false;
	    if (tocLayerManager.isManzanaLayerInEdition()) {
		//TODO: save layer
		//te.stopEditing(sourceLayer, false);
		sourceLayer.setActive(false);
		wasEditingManzanas = true;
	    }
	    destinationLayer.setActive(true);
	    te.startEditing(destinationLayer);
	    FBitSet indexes = sourceLayer.getRecordset().getSelection();
	    IFeature feature = sourceLayer.getSource().getFeature(indexes.nextSetBit(0));
	    te.addGeometryWithParametrizedValues(feature.getGeometry(), feature.getAttributes().clone(), "_create_new_predio");
	    //TODO: save layer
	    //te.stopEditing(destinationLayer, false);
	    destinationLayer.setActive(false);
	    if (wasEditingManzanas) {
		//te.startEditing(sourceLayer);
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
