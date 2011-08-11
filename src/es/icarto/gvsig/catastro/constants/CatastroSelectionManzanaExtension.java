package es.icarto.gvsig.catastro.constants;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;


public class CatastroSelectionManzanaExtension extends Extension {

    TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
    }

    @Override
    public void execute(String actionCommand) {
	tocLayerManager = new TOCLayerManager();
	tocLayerManager.setActiveAndVisibleLayer(Preferences.MANZANAS_LAYER_NAME);
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	MapControl mc = view.getMapControl();
	if (!mc.getNamesMapTools().containsKey("constantsSelectionPoint")) {
	    ConstantsSelectionListener csl = new ConstantsSelectionListener(mc);
	    mc.addMapTool("constantsSelectionPoint", new PointBehavior(csl));
	}
	mc.setTool("constantsSelectionPoint");
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
