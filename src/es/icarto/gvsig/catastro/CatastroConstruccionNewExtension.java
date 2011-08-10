package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class CatastroConstruccionNewExtension extends Extension {

    private InsertAreaWrapper insertAreaWrapper;
    private TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
	insertAreaWrapper = new InsertAreaWrapper();
	insertAreaWrapper.initialize();
    }

    @Override
    public void execute(String actionCommand) {
	tocLayerManager = new TOCLayerManager();
	tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	insertAreaWrapper.execute(actionCommand);
    }

    @Override
    public boolean isEnabled() {
	return insertAreaWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return insertAreaWrapper.isVisible();
    }
}
