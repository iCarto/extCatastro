package es.icarto.gvsig.catastro.wrappers;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.InsertAreaExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class InsertAreaWrapper extends InsertAreaExtension {
	
	public void initialize() {
		super.initialize();
	}
	
	public void execute(String s) {
		ToggleEditing te = new ToggleEditing();
		FLayer activeLayer = getActiveLayer();
		if (!activeLayer.isEditing()) {
			te.startEditing(activeLayer);
		}
		super.execute(s);
	}
		
	public boolean isEnabled() {
		return true;
	}

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
