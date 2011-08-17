package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;
import java.util.List;

import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.util.PolygonExtracter;


public class ConstruccionDivideFeaturesRetriever {

    private IFeature construccion = null;
    private ArrayList<IGeometry> predios = null;
    private ArrayList<IFeature> featuresToModify = null;
    private ArrayList<IFeature> featuresToAdd = null;
    private int idNewPredio = -1;

    public ConstruccionDivideFeaturesRetriever(IFeature construccion,
	    ArrayList<IGeometry> geoms, int idNewPredio) {
	this.construccion = construccion;
	this.predios = geoms;
	this.featuresToModify = new ArrayList<IFeature>();
	this.featuresToAdd = new ArrayList<IFeature>();
	this.idNewPredio = idNewPredio;
    }

    public void execute() {
	Geometry prediosIntersection = getPrediosIntersection();
	Polygon[] construcciones = getConstrucciones();
	for (int j = 0; j < construcciones.length; j++) {
	    Polygon edificio = construcciones[j];
	    Geometry lineOfIntersection = prediosIntersection
		    .intersection(edificio);
	    if (isIntersecting(lineOfIntersection, edificio)) {
		ConstruccionDivideUpdateGeomAndValues construccionesCutter = new ConstruccionDivideUpdateGeomAndValues(
			idNewPredio);
		// cut the construccion by the 1st predio, which have the old ID
		// the second predio will have a new created ID
		if (construccionesCutter.execute(construccion, predios.get(0))) {
		    featuresToModify.add(construccionesCutter.getOldEdificio());
		    featuresToAdd.add(construccionesCutter.getNewEdificio());
		}
	    }
	}
    }

    private boolean isIntersecting(Geometry line, Polygon polygon) {
	String lineType = line.getGeometryType();
	if ((line.getNumGeometries() > 0)
		&& ((lineType.equalsIgnoreCase("LineString")) || (lineType
			.equalsIgnoreCase("MultiLineString")))) {
	    return true;
	}
	return false;
    }

    private Polygon[] getConstrucciones() {
	Geometry geom = construccion.getGeometry().toJTSGeometry();
	return extractPolygons(geom);
    }

    private Geometry getPrediosIntersection() {
	Geometry geom1 = predios.get(0).toJTSGeometry();
	Geometry geom2 = predios.get(1).toJTSGeometry();
	return geom1.intersection(geom2);
    }

    public ArrayList<IFeature> getFeaturesToModify() {
	return featuresToModify;
    }

    public ArrayList<IFeature> getFeaturesToAdd() {
	return featuresToAdd;
    }

    public Polygon[] extractPolygons(Geometry g) {
	Polygon[] solution = null;
	List<Polygon> solutionList = new ArrayList<Polygon>();
	if (g instanceof Polygon) {
	    solutionList.add((Polygon) g);
	} else if (g instanceof GeometryCollection) {
	    GeometryCollection geomCol = (GeometryCollection) g;
	    List polygons = PolygonExtracter.getPolygons(geomCol);
	    solutionList.addAll(polygons);
	}
	solution = new Polygon[solutionList.size()];
	solutionList.toArray(solution);
	return solution;
    }
}
