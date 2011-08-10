package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import es.icarto.gvsig.catastro.utils.CatastroUtils;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class CatastroConstruccionNewExtension extends Extension {
	
	private InsertAreaWrapper insertAreaWrapper;
	private FLayer layer;

	@Override
	public void execute(String actionCommand) {
		layer = CatastroUtils.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
		layer.setActive(true);
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
