package es.icarto.gvsig.catastro.evaluator;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.evaluator.actions.IAction;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class UpdateConstruccionesAffected implements IAction {

    private ArrayList<IGeometry> predios;
    private FLyrVect construccionesLayer;
    private static FLyrVect prediosLayer;
    private int idNewPredio = -1;
    private ToggleEditing te;
    private ArrayList<IFeature> construccionesAffected;

    public UpdateConstruccionesAffected(FLyrVect prediosLayer,
	    ArrayList<IGeometry> predios, int idNewPredio) {
	this.predios = predios;
	this.idNewPredio = idNewPredio;
	this.prediosLayer = prediosLayer;
	te = new ToggleEditing();
	init();
    }

    private void init() {
	this.construccionesLayer = getConstrucciones();
	construccionesAffected = getConstruccionesAffected();
    }

    @Override
    public boolean execute() {
	prediosLayer.setActive(false);
	construccionesLayer.setActive(true);
	if (prediosLayer.isEditing()) {
	    te.stopEditing(prediosLayer, false);
	}
	te.startEditing(construccionesLayer);
	for (IFeature construccion : construccionesAffected) {
	    if (!save(construccion)) {
		construccionesLayer.setActive(false);
		prediosLayer.setActive(true);
		return false;
	    }
	}
	te.stopEditing(construccionesLayer, false);
	construccionesLayer.setActive(false);
	prediosLayer.setActive(true);
	return true;
    }

    private FLyrVect getConstrucciones() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager
		.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
    }

    private boolean save(IFeature construccion) {
	ConstruccionesCutter construccionesCutter = new ConstruccionesCutter(
		construccion, predios, idNewPredio);
	construccionesCutter.execute();
	ArrayList<IFeature> featuresToModify = construccionesCutter
		.getFeaturesToModify();
	ArrayList<IFeature> featuresToAdd = construccionesCutter
		.getFeaturesToAdd();
	for (IFeature feature : featuresToModify) {
	    te.modifyFeature(Integer.parseInt(feature.getID()), feature,
		    "_cutConstruccionesAffected");
	}
	for (IFeature feature : featuresToAdd) {
	    te.addGeometryWithParametrizedValues(feature.getGeometry(),
		    feature.getAttributes(), "_cutConstrucciones");
	}
	return true;
    }

    private ArrayList<IFeature> getConstruccionesAffected() {
	ArrayList<IFeature> features = new ArrayList<IFeature>();
	Geometry intersection = getIntersection();
	if (isIntersectionAsExpected(intersection)) {
	    try {
		Rectangle2D intersectionBoundingBox = getBoundingBoxOfIntersection(intersection);
		IFeatureIterator neighboringFeatures = getNeighboringFeatures(
			construccionesLayer, intersectionBoundingBox);
		while (neighboringFeatures.hasNext()) {
		    IFeature feature = neighboringFeatures.next();
		    Rectangle2D featureBoundingBox = getFeatureBoundingBox(feature);
		    if (intersectionBoundingBox.intersects(featureBoundingBox)) {
			features.add(feature);
		    }
		}
		return features;
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		return null;
	    }
	}
	return null;
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_cut_constructions_not_possible");
    }

    private Geometry getIntersection() {
	Geometry geom1 = predios.get(0).toJTSGeometry();
	Geometry geom2 = predios.get(1).toJTSGeometry();
	return geom1.intersection(geom2);
    }

    private boolean isIntersectionAsExpected(Geometry prediosIntersection) {
	return prediosIntersection.getGeometryType().equalsIgnoreCase(
		"LineString")
		|| prediosIntersection.getGeometryType().equalsIgnoreCase(
			"MultiLineString");
    }

    private Rectangle2D getBoundingBoxOfIntersection(
	    Geometry prediosIntersection) {
	Envelope envelope = prediosIntersection.getEnvelopeInternal();
	double delta = 10; // offset to get geometries touching the real
			   // envelope
	double minX = envelope.getMinX() - delta;
	double minY = envelope.getMinY() - delta;
	double maxX = envelope.getMaxX() + delta;
	double maxY = envelope.getMaxY() + delta;
	Rectangle2D boundingBox = new Rectangle2D.Double(minX, minY, maxX
		- minX, maxY - minY);
	return boundingBox;
    }

    private IFeatureIterator getNeighboringFeatures(
	    FLyrVect construccionesLayer2, Rectangle2D intersectionBoundingBox) {
	try {
	    return construccionesLayer.getSource().getFeatureIterator(
		    intersectionBoundingBox, null, null, false);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private Rectangle2D getFeatureBoundingBox(IFeature feature) {
	return feature.getGeometry().getBounds2D();
    }

}
