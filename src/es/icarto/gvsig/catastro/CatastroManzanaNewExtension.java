package es.icarto.gvsig.catastro;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.CatastroUtils;
import es.icarto.gvsig.catastro.utils.Preferences;

import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class CatastroManzanaNewExtension extends Extension {

    private InsertAreaWrapper insertAreaWrapper;
    private FLayer layer;
    private ConstantManager constantManager;

    @Override
    public void initialize() {
	insertAreaWrapper = new InsertAreaWrapper();
	insertAreaWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
	if(constantManager.areConstantsSetForManzana()){
	    layer = CatastroUtils.getLayerByName(Preferences.MANZANAS_LAYER_NAME);
	    layer.setActive(true);
	    insertAreaWrapper.execute(actionCommand);
	} else{
	    Object[] options = { "OK" };
	    JOptionPane.showOptionDialog(null, PluginServices.getText(this, "select_region"), "Warning",
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
