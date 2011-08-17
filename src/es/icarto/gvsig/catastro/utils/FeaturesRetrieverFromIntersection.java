package es.icarto.gvsig.catastro.utils;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/*
 * This class computes the line intersection of 2 geometries 
 * and returns the features in the layer given which intersects with it.
 * Also check that the intersection is linestring or multilinestring JTS types. 
 */
public class FeaturesRetrieverFromIntersection {

    private ArrayList<IGeometry> geoms = null;
    private FLyrVect layer = null;
    private ArrayList<IFeature> features = null;

    public FeaturesRetrieverFromIntersection(FLyrVect layer,
	    ArrayList<IGeometry> geoms) {
	this.geoms = geoms;
	this.layer = layer;
	this.features = new ArrayList<IFeature>();
    }

    public ArrayList<IFeature> getFeatures() {
	Geometry intersection = getIntersection();
	if (isIntersectionAsExpected(intersection)) {
	    try {
		Rectangle2D intersectionBoundingBox = getBoundingBoxOfIntersection(intersection);
		IFeatureIterator neighboringFeatures = getNeighboringFeatures(
			layer, intersectionBoundingBox);
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

    private Rectangle2D getFeatureBoundingBox(IFeature feature) {
	return feature.getGeometry().getBounds2D();
    }

    private IFeatureIterator getNeighboringFeatures(
	    FLyrVect construccionesLayer2, Rectangle2D intersectionBoundingBox) {
	try {
	    return layer.getSource().getFeatureIterator(
		    intersectionBoundingBox, null, null, false);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
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

    private boolean isIntersectionAsExpected(Geometry prediosIntersection) {
	return prediosIntersection.getGeometryType().equalsIgnoreCase(
		"LineString")
		|| prediosIntersection.getGeometryType().equalsIgnoreCase(
			"MultiLineString");
    }

    private Geometry getIntersection() {
	Geometry geom1 = geoms.get(0).toJTSGeometry();
	Geometry geom2 = geoms.get(1).toJTSGeometry();
	return geom1.intersection(geom2);
    }

}
