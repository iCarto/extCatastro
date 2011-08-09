package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.wrappers.InsertAreaWrapper;

public class InsertAreaCatastroExtension extends Extension {
	
	private InsertAreaWrapper insertAreaWrapper;

	@Override
	public void execute(String actionCommand) {
		insertAreaWrapper.execute(actionCommand);
	}

	@Override
	public void initialize() {
		insertAreaWrapper = new InsertAreaWrapper();
		insertAreaWrapper.initialize();
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
