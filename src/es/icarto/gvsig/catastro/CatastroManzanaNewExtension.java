package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class CatastroManzanaNewExtension extends Extension {

	private InsertAreaWrapper insertAreaWrapper;
	private TOCLayerManager tocLayerManager;

	@Override
	public void execute(String actionCommand) {
		tocLayerManager = new TOCLayerManager();
		tocLayerManager.setActiveAndVisibleLayersForManzanas();
		insertAreaWrapper.execute(actionCommand);
	}

	@Override
	public void initialize() {
		insertAreaWrapper = new InsertAreaWrapper();
		insertAreaWrapper.initialize();
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
