package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import es.icarto.gvsig.catastro.utils.CatastroUtils;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.wrappers.SelectionGeometryWrapper;

public class SelectionGeometryCatastroConstruccionExtension extends Extension {
	
	private SelectionGeometryWrapper selectionGeometryWrapper;
	private FLayer layer;

	@Override
	public void execute(String actionCommand) {
		layer = CatastroUtils.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
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
}
