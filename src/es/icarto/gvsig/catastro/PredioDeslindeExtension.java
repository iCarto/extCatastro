package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.wrapperscadtools.CutPolygonWrapper;

public class PredioDeslindeExtension extends Extension {

    private CutPolygonWrapper cutPolygonWrapper;
    private ConstantManager constantManager;

    @Override
    public void initialize() {
	cutPolygonWrapper = new CutPolygonWrapper();
	cutPolygonWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
    }

    @Override
    public boolean isEnabled() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return cutPolygonWrapper.isVisible();
    }

}
