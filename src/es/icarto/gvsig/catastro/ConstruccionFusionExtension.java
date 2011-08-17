package es.icarto.gvsig.catastro;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrapperscadtools.SelectionGeometryWrapper;

public class ConstruccionFusionExtension extends Extension {

    private SelectionGeometryWrapper selectionGeometryWrapper;
    private TOCLayerManager tocLayerManager;
    private ConstantManager constantManager;

    @Override
    public void initialize() {
	selectionGeometryWrapper = new SelectionGeometryWrapper();
	selectionGeometryWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
	if(constantManager.areConstantsSetForConstruccion()){
	    tocLayerManager = new TOCLayerManager();
	    tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	    selectionGeometryWrapper.execute(actionCommand);
	} else{
	    Object[] options = { "OK" };
	    JOptionPane.showOptionDialog(null, PluginServices.getText(this, "select_predio"), "Warning",
		    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, options, options[0]);
	}
    }

    @Override
    public boolean isEnabled() {
	return false;
	//	return selectionGeometryWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return selectionGeometryWrapper.isVisible();
    }
}
