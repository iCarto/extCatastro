package es.icarto.gvsig.catastro;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.wrappers.CutPolygonWrapper;

public class CutPolygonCatastroConstruccionExtension extends Extension {
	
	private CutPolygonWrapper cutPolygonWrapper;
	private FLayer layer;
	
	@Override
	public void execute(String actionCommand) {
		layer = getLayerByName();
		layer.setActive(true);
		cutPolygonWrapper.execute(actionCommand);
		
	}

	@Override
	public void initialize() {
		cutPolygonWrapper = new CutPolygonWrapper();
		cutPolygonWrapper.initialize();
	}

	@Override
	public boolean isEnabled() {
		return cutPolygonWrapper.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return cutPolygonWrapper.isVisible();
	}

	private FLayer getLayerByName() {
		BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
		MapControl mapControl = view.getMapControl();
		FLayers flayers = mapControl.getMapContext().getLayers();
		flayers.setAllActives(false);
		FLyrVect actLayer = null;
		for (int i = 0; i < flayers.getLayersCount(); i++) {
			if (flayers.getLayer(i).getName().equalsIgnoreCase(Preferences.CONSTRUCCIONES_LAYER_NAME)) {
				actLayer = (FLyrVect) flayers.getLayer(i);
			}
		}
		return actLayer;
	}
}
