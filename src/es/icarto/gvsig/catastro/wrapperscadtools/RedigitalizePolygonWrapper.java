package es.icarto.gvsig.catastro.wrapperscadtools;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.RedigitalizePolygonExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class RedigitalizePolygonWrapper extends RedigitalizePolygonExtension {

    @Override
    public void initialize() {
	super.initialize();
    }

    @Override
    public void execute(String s) {
	ToggleEditing te = new ToggleEditing();
	FLayer activeLayer = getActiveLayer();
	if (!activeLayer.isEditing()) {
	    te.startEditing(activeLayer);
	}
	super.execute(s);
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    private FLayer getActiveLayer() {
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers flayers = mapControl.getMapContext().getLayers();
	FLyrVect actLayer = null;
	for (int i = 0; i < flayers.getActives().length; i++) {

	    if (!(flayers.getActives()[i] instanceof FLayers)) {
		actLayer = (FLyrVect) flayers.getActives()[i];
	    }
	}
	return actLayer;
    }

}
