package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class CutConstruccionesByPredio {

    private FLyrVect layer;
    public CutConstruccionesByPredio(FLyrVect construccionesLayer){
	this.layer = construccionesLayer;
    }

    public boolean clip(IFeature edificio, IGeometry predio) {
	/* TODO:
	 * - get the 2 polygons
	 *   - 1st: by intersecting the predio and the edificio
	 *   - 2nd: by difference between the 1st polygon and the edificio
	 * - save 1 of them with those values + updating area/length
	 * - save the other updating the ID + updating area/length
	 */
	int index = Integer.parseInt(edificio.getID())-1;
	Geometry edificioJTSFirst = predio.toJTSGeometry().intersection(edificio.getGeometry().toJTSGeometry());
	if(edificioJTSFirst.getGeometryType().equalsIgnoreCase("Polygon")){
	    Geometry edificioJTSSecond = edificio.getGeometry().toJTSGeometry().difference(edificioJTSFirst);
	    IGeometry edificioFirst = FConverter.jts_to_igeometry(edificioJTSFirst);
	    IGeometry edificioSecond = FConverter.jts_to_igeometry(edificioJTSSecond);
	    try {
		Value[] valuesEdificio1 = layer.getRecordset().getRow(index).clone();
		Value[] valuesEdificio2 = layer.getRecordset().getRow(index).clone();
		DefaultFeature df = new DefaultFeature(edificioFirst, valuesEdificio1, Integer.toString(index));
		ToggleEditing te = new ToggleEditing();
		layer.setActive(true);
		TOCLayerManager tocLayerManager = new TOCLayerManager();
		tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME).setActive(false);
		if(!layer.isEditing()){
		    te.startEditing(layer);
		}
		te.modifyFeature(index, df, "_cutConstrucciones");
		valuesEdificio2[8] = getNewIDForConstrucciones();
		te.addGeometryWithParametrizedValues(edificioSecond, valuesEdificio2, "_cutConstrucciones");
		//TODO: enable saving
		//te.stopEditing(layer, false);
		layer.setActive(false);
		tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME).setActive(true);
	    } catch (NumberFormatException e) {
		e.printStackTrace();
	    } catch (ReadDriverException e) {
		e.printStackTrace();
	    }
	}
	return true;
    }

    private Value getNewIDForConstrucciones() {
	CalculateIDNewConstruccion calculateID = new CalculateIDNewConstruccion(layer);
	calculateID.execute();
	return calculateID.getNewConstruccionID();
    }

}
