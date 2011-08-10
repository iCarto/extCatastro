package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.CutPolygonWrapper;

public class CatastroConstruccionDivideExtension extends Extension {

    private CutPolygonWrapper cutPolygonWrapper;
    private TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
	cutPolygonWrapper = new CutPolygonWrapper();
	cutPolygonWrapper.initialize();
    }

    @Override
    public void execute(String actionCommand) {
	tocLayerManager = new TOCLayerManager();
	tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	cutPolygonWrapper.execute(actionCommand);
    }

    @Override
    public boolean isEnabled() {
	return cutPolygonWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return cutPolygonWrapper.isVisible();
    }
}
