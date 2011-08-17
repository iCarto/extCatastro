package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CutConstruccionesByPredio {

    private FLyrVect layer;
    private IFeature newEdificio = null;
    private IFeature oldEdificio = null;
    private int idNewPredio = -1;

    public CutConstruccionesByPredio(int idNewPredio) {
	this.idNewPredio = idNewPredio;
	this.layer = getConstruccionesLayer();
    }

    private FLyrVect getConstruccionesLayer() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager
		.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
    }

    public boolean execute(IFeature construccion, IGeometry predio) {
	/*
	 * Steps of the algorithm: 1) get the 2 polygons 2) 1st: by intersecting
	 * the predio and the edificio 3) 2nd: by difference between the 1st
	 * polygon and the edificio 4) save 1 of them with those values +
	 * updating area/length 5) save the other updating the ID + updating
	 * area/length
	 */
	Geometry edificioWithOldID = predio.toJTSGeometry().intersection(
		construccion.getGeometry().toJTSGeometry());
	if (edificioWithOldID.getGeometryType().equalsIgnoreCase("Polygon")) {
	    Geometry edificioWithNewID = construccion.getGeometry()
		    .toJTSGeometry().difference(edificioWithOldID);
	    try {
		IGeometry geomEdificioWithOldID = FConverter
			.jts_to_igeometry(edificioWithOldID);
		IGeometry geomEdificioWithNewID = FConverter
			.jts_to_igeometry(edificioWithNewID);
		int index = Integer.parseInt(construccion.getID()) - 1;
		Value[] valuesEdificioWithOldID = layer.getRecordset()
			.getRow(index).clone();
		Value[] valuesEdificioWithNewID = layer.getRecordset()
			.getRow(index).clone();
		// update values for new construccion
		// update ID predio containing it: pre_cve
		valuesEdificioWithNewID[6] = ValueFactory
			.createValue(idNewPredio);
		// reset cons_id for new edificio as it will be set in the DB
		valuesEdificioWithNewID[8] = ValueFactory.createNullValue();
		// reset gid for new edificio as it will be set in the DB
		valuesEdificioWithNewID[11] = ValueFactory.createNullValue();
		oldEdificio = new DefaultFeature(geomEdificioWithOldID,
			valuesEdificioWithOldID, construccion.getID());
		newEdificio = new DefaultFeature(geomEdificioWithNewID,
			valuesEdificioWithNewID, getNewIndex());
		return true;
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		return false;
	    }
	}
	return false;
    }

    private String getNewIndex() {
	VectorialLayerEdited vle = (VectorialLayerEdited) CADExtension
		.getEditionManager().getActiveLayerEdited();
	return vle.getVEA().getNewFID();
    }

    private Value getNewIDForConstrucciones() {
	CalculateIDNewConstruccion calculateID = new CalculateIDNewConstruccion(
		layer);
	calculateID.execute();
	return calculateID.getNewConstruccionID();
    }

    public IFeature getOldEdificio() {
	return oldEdificio;
    }

    public IFeature getNewEdificio() {
	return newEdificio;
    }

}
