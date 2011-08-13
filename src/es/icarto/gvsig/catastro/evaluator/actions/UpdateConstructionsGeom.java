package es.icarto.gvsig.catastro.evaluator.actions;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.jts.JtsUtil;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;


public class UpdateConstructionsGeom implements IAction {

    private ArrayList<IGeometry> predios;
    private FLyrVect construccionesLayer;

    public UpdateConstructionsGeom(ArrayList<IGeometry> geometries){
	predios = geometries;
	construccionesLayer = getConstrucciones();
    }

    private FLyrVect getConstrucciones() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
    }

    @Override
    public boolean execute() {
	if(dividingPrediosAffectedConstrucciones()){
	    return true;
	} else{
	    return true;
	}
    }

    private boolean dividingPrediosAffectedConstrucciones() {
	Geometry geom1 = predios.get(0).toJTSGeometry();
	Geometry geom2 = predios.get(1).toJTSGeometry();
	Geometry prediosIntersection = geom1.intersection(geom2);
	if(prediosIntersection.getGeometryType().equalsIgnoreCase("LineString") ||
		prediosIntersection.getGeometryType().equalsIgnoreCase("MultiLineString")){
	    Envelope envelope = prediosIntersection.getEnvelopeInternal();
	    double delta = 10; //offset to get geometries touching the real envelope
	    double minX = envelope.getMinX() - delta;
	    double minY = envelope.getMinY() - delta;
	    double maxX = envelope.getMaxX() + delta;
	    double maxY = envelope.getMaxY() + delta;
	    Rectangle2D boundingBox = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	    try {
		IFeatureIterator neighboringGeometries = construccionesLayer.getSource().getFeatureIterator(boundingBox, null, null, false);
		while (neighboringGeometries.hasNext()) {
		    IFeature feature = neighboringGeometries.next();
		    IGeometry geometry = feature.getGeometry();
		    Rectangle2D boundingBoxOfConstruccionGeom = geometry.getBounds2D();
		    if (boundingBox.intersects(boundingBoxOfConstruccionGeom)) {
			Geometry construccionGeom = geometry.toJTSGeometry();
			Polygon[] construccionPolygons = JtsUtil.extractPolygons(construccionGeom);
			int numGeometries = construccionPolygons.length;
			for (int j = 0; j < numGeometries; j++) {
			    Polygon edificio = construccionPolygons[j];
			    Geometry intersectionWithEdificio = prediosIntersection.intersection(edificio);
			    String isIntersecting = intersectionWithEdificio.getGeometryType();
			    if((intersectionWithEdificio.getNumGeometries() > 0) &&
				    ((isIntersecting.equalsIgnoreCase("LineString")) || (isIntersecting.equalsIgnoreCase("MultiLineString")))){
				System.out.println(" --- Area edificio toca: " + edificio.getArea());
				System.out.println(" --- Length edificio toca: " + edificio.getLength());
				//cutConstruccionesWithPrediosDivision();
				return true;
			    }
			}
		    }
		}
		return false;
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		return false;
	    }
	}
	return false;
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this, "action_cut_constructions_not_possible");
    }

}
