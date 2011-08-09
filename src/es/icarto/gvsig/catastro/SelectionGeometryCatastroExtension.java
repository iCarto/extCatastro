package es.icarto.gvsig.catastro;

import com.iver.andami.plugins.Extension;

public class SelectionGeometryCatastroExtension extends Extension {

	private SelectionGeometryWrapper selectionGeometryWrapper;

	@Override
	public void execute(String actionCommand) {
		selectionGeometryWrapper.execute(actionCommand);
	}

	@Override
	public void initialize() {
		selectionGeometryWrapper = new SelectionGeometryWrapper();
		selectionGeometryWrapper.initialize();
	}

	@Override
	public boolean isEnabled() {
		return selectionGeometryWrapper.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return selectionGeometryWrapper.isVisible();
	}

}
