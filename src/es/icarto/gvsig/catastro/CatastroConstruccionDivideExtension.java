package es.icarto.gvsig.catastro;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.CutPolygonWrapper;

public class CatastroConstruccionDivideExtension extends Extension {

    private CutPolygonWrapper cutPolygonWrapper;
    private TOCLayerManager tocLayerManager;
    private ConstantManager constantManager;

    @Override
    public void initialize() {
	cutPolygonWrapper = new CutPolygonWrapper();
	cutPolygonWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
	if(constantManager.areConstantsSetForConstruccion()){
	    tocLayerManager = new TOCLayerManager();
	    tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	    cutPolygonWrapper.execute(actionCommand);
	} else{
	    Object[] options = { "OK" };
	    JOptionPane.showOptionDialog(null, PluginServices.getText(this, "select_predio"), "Warning",
		    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, options, options[0]);
	}
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
