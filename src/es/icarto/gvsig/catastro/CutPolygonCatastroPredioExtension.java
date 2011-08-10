package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import es.icarto.gvsig.catastro.utils.CatastroUtils;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.wrappers.CutPolygonWrapper;

public class CutPolygonCatastroPredioExtension extends Extension {
	
	private CutPolygonWrapper cutPolygonWrapper;
	private FLayer layer;
	
	@Override
	public void execute(String actionCommand) {
		layer = CatastroUtils.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
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
}
