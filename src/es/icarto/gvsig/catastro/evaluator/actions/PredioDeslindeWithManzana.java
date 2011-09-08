package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class PredioDeslindeWithManzana implements IAction {

    private final IGeometry redigitalizedPredioGeom;
    private IFeature redigitalizedManzana = null;
    private FLyrVect manzanasLayer = null;

    public PredioDeslindeWithManzana(IGeometry redigitalizedPredioGeom) {
	this.redigitalizedPredioGeom = redigitalizedPredioGeom;
	this.manzanasLayer = getManzanasLayer();
    }

    @Override
    public boolean execute() {
	Geometry manzanaJTSGeom = getManzanaGeom();
	Geometry redigitalizedPredioJTSGeom = redigitalizedPredioGeom
		.toJTSGeometry();
	Geometry newManzanaJTSGeom = manzanaJTSGeom
		.difference(redigitalizedPredioJTSGeom);
	IGeometry newManzanaGeom = FConverter
		.jts_to_igeometry(newManzanaJTSGeom);
	Value[] valuesManzana;
	try {
	    int rowIndex = getIndexOfRow();
	    SelectableDataSource sds = manzanasLayer.getRecordset();
	    int col = sds.getFieldIndexByName(Preferences.GID_IN_DB);
	    ToggleEditing te = new ToggleEditing();
	    te.startEditing(getManzanasLayer());
	    valuesManzana = getManzanasLayer().getRecordset().getRow(rowIndex)
		    .clone();
	    redigitalizedManzana = new DefaultFeature(newManzanaGeom,
		    valuesManzana, valuesManzana[col].toString());
	    te.modifyFeature(rowIndex + 1, redigitalizedManzana,
		    "_redigitalizedPredio");
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private int getIndexOfRow() {
	IFeature manzana = getGeomFromFLyrVect(manzanasLayer);
	SelectableDataSource sds;
	try {
	    sds = manzanasLayer.getRecordset();
	    int col = sds.getFieldIndexByName(Preferences.GID_IN_DB);
	    for (int row = 0; row < sds.getRowCount(); row++) {
		if (sds.getFieldValue(row, col).toString()
			.equalsIgnoreCase(manzana.getAttribute(col).toString())) {
		    return row;
		}
	    }
	    return -1;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return -1;
	}
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_deslinde_predio_with_manzana_was_not_performed");
    }

    private FLyrVect getManzanasLayer() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager.getLayerByName(Preferences.MANZANAS_LAYER_NAME);
    }

    private Geometry getManzanaGeom() {
	FLyrVect manzanasLayer = getManzanasLayer();
	IGeometry manzanaGeom = getGeomFromFLyrVect(manzanasLayer)
		.getGeometry();
	Geometry manzanaJTSGeom = manzanaGeom.toJTSGeometry();
	return manzanaJTSGeom;
    }

    private IFeature getGeomFromFLyrVect(FLyrVect layer) {
	ConstantManager constantManager = new ConstantManager();
	try {
	    String sqlQuery = "select * from '"
		    + layer.getRecordset().getName() + "'" + " where "
		    + Preferences.PAIS_NAME_IN_DB + " = "
		    + constantManager.getConstants().getPais() + " " + " and "
		    + Preferences.ESTADO_NAME_IN_DB + " = "
		    + constantManager.getConstants().getEstado() + " "
		    + " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = "
		    + constantManager.getConstants().getMunicipio() + " "
		    + " and " + Preferences.LIMITE_NAME_IN_DB + " = "
		    + constantManager.getConstants().getLimiteMunicipal() + " "
		    + " and " + Preferences.MANZANA_NAME_IN_DB + " = "
		    + constantManager.getConstants().getManzana() + " "
		    + " and " + Preferences.REGION_NAME_IN_DB + " = "
		    + constantManager.getConstants().getRegion() + ";";
	    IFeatureIterator featureIterator = layer.getSource()
		    .getFeatureIterator(sqlQuery, null);
	    if (featureIterator.hasNext()) {
		return featureIterator.next();
	    } else {
		return null;
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
