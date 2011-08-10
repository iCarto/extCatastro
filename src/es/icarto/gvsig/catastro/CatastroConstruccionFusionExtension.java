package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.SelectionGeometryWrapper;

public class CatastroConstruccionFusionExtension extends Extension {

    private SelectionGeometryWrapper selectionGeometryWrapper;
    private TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
	selectionGeometryWrapper = new SelectionGeometryWrapper();
	selectionGeometryWrapper.initialize();
    }

    @Override
    public void execute(String actionCommand) {
	tocLayerManager = new TOCLayerManager();
	tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	selectionGeometryWrapper.execute(actionCommand);
    }


    @Override
    public boolean isEnabled() {
	return selectionGeometryWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return selectionGeometryWrapper.isVisible();
    }
}
