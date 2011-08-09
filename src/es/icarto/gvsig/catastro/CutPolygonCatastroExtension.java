package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.wrappers.CutPolygonWrapper;

public class CutPolygonCatastroExtension extends Extension {
	
	private CutPolygonWrapper cutPolygonWrapper;
	
	@Override
	public void execute(String actionCommand) {
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
