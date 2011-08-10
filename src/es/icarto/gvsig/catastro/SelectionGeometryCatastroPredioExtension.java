package es.icarto.gvsig.catastro;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.icarto.gvsig.catastro.wrappers.SelectionGeometryWrapper;

public class SelectionGeometryCatastroPredioExtension extends Extension {
	
	private static final String LAYER_NAME = "predios";

	private SelectionGeometryWrapper selectionGeometryWrapper;
	private FLayer layer;

	@Override
	public void execute(String actionCommand) {
		layer = getLayerByName();
		layer.setActive(true);
		selectionGeometryWrapper.execute(actionCommand);
	}

	@Override
	public void initialize() {
		selectionGeometryWrapper = new SelectionGeometryWrapper();
		selectionGeometryWrapper.initialize();
	}

	@Override
	public boolean isEnabled() {
		return selectionGeometryWrapper.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return selectionGeometryWrapper.isVisible();
	}
	
	private FLayer getLayerByName() {
		BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
		MapControl mapControl = view.getMapControl();
		FLayers flayers = mapControl.getMapContext().getLayers();
		flayers.setAllActives(false);
		FLyrVect actLayer = null;
		for (int i = 0; i < flayers.getLayersCount(); i++) {
			if (flayers.getLayer(i).getName().equalsIgnoreCase(LAYER_NAME)) {
				actLayer = (FLyrVect) flayers.getLayer(i);
			}
		}
		return actLayer;
	}

}
