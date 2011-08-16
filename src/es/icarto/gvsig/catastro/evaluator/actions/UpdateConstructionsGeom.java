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
	if(dividingConstruccionesAffectedByDividingPredios()){
	    return true;
	} else{
	    return true;
	}
    }

    private boolean dividingConstruccionesAffectedByDividingPredios() {
	Geometry prediosIntersection = getPrediosIntersection();
	if(prediosIntersection.getGeometryType().equalsIgnoreCase("LineString") ||
		prediosIntersection.getGeometryType().equalsIgnoreCase("MultiLineString")){

	    Rectangle2D intersectionBoundingBox = getBoundingBoxOfIntersection(prediosIntersection);
	    try {
		IFeatureIterator neighboringConstructions = construccionesLayer.getSource().getFeatureIterator(intersectionBoundingBox, null, null, false);
		while (neighboringConstructions.hasNext()) {
		    IFeature construccionIFeature = neighboringConstructions.next();
		    IGeometry construccionIGeometry = construccionIFeature.getGeometry();
		    Rectangle2D construccionBoundingBox = construccionIGeometry.getBounds2D();
		    if (intersectionBoundingBox.intersects(construccionBoundingBox)) {
			Geometry construccionGeom = construccionIGeometry.toJTSGeometry();
			Polygon[] construccionPolygons = JtsUtil.extractPolygons(construccionGeom);
			int numGeometries = construccionPolygons.length;
			for (int j = 0; j < numGeometries; j++) {
			    Polygon edificio = construccionPolygons[j];
			    Geometry lineOfIntersection = prediosIntersection.intersection(edificio);
			    String isIntersecting = lineOfIntersection.getGeometryType();
			    if((lineOfIntersection.getNumGeometries() > 0) &&
				    ((isIntersecting.equalsIgnoreCase("LineString")) || (isIntersecting.equalsIgnoreCase("MultiLineString")))){
				ConstruccionesCutter construccionesCutter = new ConstruccionesCutter(construccionesLayer);
				//Clip each intersecting construccion by means of the first predio
				//This predio will contain the old ID, so do construcciones clipped by it
				if(!construccionesCutter.clip(construccionIFeature, predios.get(0))){
				    //end the process if some construction was impossible to cut
				    return false;
				}
			    }
			}
		    }
		}
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		return false;
	    }
	}
	return true; //there is no construction to divide or all were divided well
    }

    private Rectangle2D getBoundingBoxOfIntersection(
	    Geometry prediosIntersection) {
	Envelope envelope = prediosIntersection.getEnvelopeInternal();
	double delta = 10; //offset to get geometries touching the real envelope
	double minX = envelope.getMinX() - delta;
	double minY = envelope.getMinY() - delta;
	double maxX = envelope.getMaxX() + delta;
	double maxY = envelope.getMaxY() + delta;
	Rectangle2D boundingBox = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	return boundingBox;
    }

    private Geometry getPrediosIntersection() {
	Geometry geom1 = predios.get(0).toJTSGeometry();
	Geometry geom2 = predios.get(1).toJTSGeometry();
	return geom1.intersection(geom2);
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this, "action_cut_constructions_not_possible");
    }

}
