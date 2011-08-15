package es.icarto.gvsig.catastro.evaluator.rules;

import org.gvsig.fmap.core.NewFConverter;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckGeometryIsOverlapingAnotherOne implements IRule {

    IGeometry insertedGeometry;
    String layerName;

    public CheckGeometryIsOverlapingAnotherOne(IGeometry insertedGeometry,
	    String layerName) {
	this.insertedGeometry = insertedGeometry;
	this.layerName = layerName;
    }

    @Override
    public boolean isObey() {
	boolean checkRule = false;
	Geometry jtsGeom = NewFConverter.toJtsGeometry(insertedGeometry);
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	FLyrVect layer = tocLayerManager.getLayerByName(layerName);
	SelectableDataSource layerRecordset;
	try {
	    layerRecordset = layer.getRecordset();
	    ReadableVectorial layerSourceFeats = layer.getSource();
	    for (int i = 0; i < layerRecordset.getRowCount(); i++) {
		IGeometry gvGeom = layerSourceFeats.getShape(i);
		if (!gvGeom.equals(jtsGeom)) {
		    Geometry auxJTSGeom = NewFConverter.toJtsGeometry(gvGeom);
		    if (!jtsGeom.overlaps(auxJTSGeom)) {
			checkRule = true;
		    } else {
			checkRule = false;
			break;
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return checkRule;
    }

    @Override
    public String getMessage() {
	if (layerName.compareToIgnoreCase(Preferences.MANZANAS_LAYER_NAME) == 0) {
	    return PluginServices.getText(this,
		    "rule_manzana_overlaps_another_one");
	} else {
	    // Construcciones
	    return PluginServices.getText(this,
		    "rule_construccion_overlaps_another_one");
	}

    }
}