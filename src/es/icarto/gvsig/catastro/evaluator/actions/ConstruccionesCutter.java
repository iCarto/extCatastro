package es.icarto.gvsig.catastro.evaluator.actions;

import org.gvsig.fmap.core.NewFConverter;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class ConstruccionesCutter {

    private FLyrVect layer;
    public ConstruccionesCutter(FLyrVect construccionesLayer){
	this.layer = construccionesLayer;
    }

    public boolean clip(IFeature edificio, int indexFeatEdificio, IGeometry predio) {
	/* TODO:
	 * - get the 2 polygons
	 *   - 1st: by intersecting the predio and the edificio
	 *   - 2nd: by difference between the 1st polygon and the edificio
	 * - save 1 of them with those values + updating area/length
	 * - save the other updating the ID + updating area/length
	 */
	Geometry edificioJTSFirst = predio.toJTSGeometry().intersection(edificio.getGeometry().toJTSGeometry());
	if(edificioJTSFirst.getGeometryType().equalsIgnoreCase("Polygon")){
	    Geometry edificioJTSSecond = edificio.getGeometry().toJTSGeometry().difference(edificioJTSFirst);
	    IGeometry edificioFirst = NewFConverter.jts_to_igeometry(edificioJTSFirst);
	    IGeometry edificioSecond = NewFConverter.jts_to_igeometry(edificioJTSSecond);
	    Value[] values = edificio.getAttributes().clone();
	    DefaultFeature df = new DefaultFeature(edificioFirst, edificio.getAttributes(), edificio.getID());
	    ToggleEditing te = new ToggleEditing();
	    layer.setActive(true);
	    TOCLayerManager tocLayerManager = new TOCLayerManager();
	    tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME).setActive(false);
	    if(!layer.isEditing()){
		te.startEditing(layer);
	    }
	    te.modifyFeature(indexFeatEdificio, df, "_cutConstrucciones");
	    te.addGeometryWithParametrizedValues(edificioSecond, values, "_cutConstrucciones");
	    te.stopEditing(layer, false);
	    layer.setActive(false);
	    tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME).setActive(true);
	}
	return true;
    }
}
