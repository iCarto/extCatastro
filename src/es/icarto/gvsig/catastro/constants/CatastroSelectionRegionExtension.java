package es.icarto.gvsig.catastro.constants;

import com.iver.andami.plugins.Extension;


public class CatastroSelectionRegionExtension extends Extension {

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
    }

    @Override
    public void execute(String actionCommand) {
	ConstantManager constantManager = new ConstantManager();
	Constants constants = new Constants();
	constants.setRegion("region");
	constantManager.setConstants(constants);
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
