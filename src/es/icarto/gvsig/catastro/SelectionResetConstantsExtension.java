package es.icarto.gvsig.catastro;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.constants.Constants;

public class SelectionResetConstantsExtension extends Extension {

    @Override
    public void execute(String actionCommand) {
	ConstantManager constantManager = new ConstantManager();
	Constants constants = constantManager.getConstants();
	constants.clear();
	PluginServices.getMainFrame().getStatusBar().setMessage("constants",
		"R:- M:- P:-");
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
