package es.icarto.gvsig.catastro.utils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class CatastroUtils {
	
	public static FLayer getLayerByName(String layerName) {
		BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
		MapControl mapControl = view.getMapControl();
		FLayers flayers = mapControl.getMapContext().getLayers();
		flayers.setAllActives(false);
		FLyrVect actLayer = null;
		for (int i = 0; i < flayers.getLayersCount(); i++) {
			if (flayers.getLayer(i).getName().equalsIgnoreCase(layerName)) {
				actLayer = (FLyrVect) flayers.getLayer(i);
			}
		}
		return actLayer;
	}

	
}
