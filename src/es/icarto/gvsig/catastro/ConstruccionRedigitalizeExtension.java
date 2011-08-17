package es.icarto.gvsig.catastro;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.wrapperscadtools.RedigitalizePolygonWrapper;

public class ConstruccionRedigitalizeExtension extends Extension {

    private RedigitalizePolygonWrapper redigitalizePolygonWrapper;
    private ConstantManager constantManager;
    private TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
	redigitalizePolygonWrapper = new RedigitalizePolygonWrapper();
	redigitalizePolygonWrapper.initialize();
	constantManager = new ConstantManager();
    }

    @Override
    public void execute(String actionCommand) {
	if (constantManager.areConstantsSetForManzana()) {
	    tocLayerManager = new TOCLayerManager();
	    tocLayerManager.setActiveAndVisibleLayersForConstrucciones();
	    redigitalizePolygonWrapper.execute(actionCommand);
	} else {
	    Object[] options = { "OK" };
	    JOptionPane.showOptionDialog(null, PluginServices.getText(this,
		    "select_predio"), "Warning", JOptionPane.DEFAULT_OPTION,
		    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	}
    }

    @Override
    public boolean isEnabled() {
	return redigitalizePolygonWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return redigitalizePolygonWrapper.isVisible();
    }

}
