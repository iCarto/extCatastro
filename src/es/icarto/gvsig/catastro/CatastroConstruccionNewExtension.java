package es.icarto.gvsig.catastro;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class CatastroConstruccionNewExtension extends Extension {

    private InsertAreaWrapper insertAreaWrapper;
    private TOCLayerManager tocLayerManager;
    private ConstantManager constantManager;

    @Override
    public void initialize() {
	insertAreaWrapper = new InsertAreaWrapper();
	insertAreaWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
	if(constantManager.areConstantsSetForConstruccion()){
	    tocLayerManager = new TOCLayerManager();
	    tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	    insertAreaWrapper.execute(actionCommand);
	} else{
	    Object[] options = { "OK" };
	    JOptionPane.showOptionDialog(null, PluginServices.getText(this, "select_predio"), "Warning",
		    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, options, options[0]);
	}

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
