package es.icarto.gvsig.catastro.evaluator.rules;

import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckAllAreaPrediosEqualsAreaManzana implements IRule {

    ConstantManager constantManager;
    public CheckAllAreaPrediosEqualsAreaManzana(){
	constantManager = new ConstantManager();
    }

    @Override
    public boolean isObey() {
	Geometry manzanaJTSGeom = getManzanaGeom();
	double areaManzana = manzanaJTSGeom.getArea();
	GeometryCollection prediosJTSGeomCollection = getPrediosGeomCollection();
	double areaPredios = prediosJTSGeomCollection.getArea();
	if((int)areaManzana == (int)areaPredios){
	    return true;
	}
	return false;
    }

    private GeometryCollection getPrediosGeomCollection() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	FLyrVect predios = tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
	IFeatureIterator prediosFeatures = getFeatureIteratorFromFLyrVect(predios);
	ArrayList<Geometry> prediosJTS = new ArrayList<Geometry>();
	try {
	    while(prediosFeatures.hasNext()){
		IGeometry geom = prediosFeatures.next().getGeometry();
		prediosJTS.add(geom.toJTSGeometry());
	    }
	    Geometry[] geoms = prediosJTS.toArray(new Geometry[prediosJTS.size()]);
	    return new GeometryCollection(geoms, new GeometryFactory());
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private IFeatureIterator getFeatureIteratorFromFLyrVect(FLyrVect layer) {
	ConstantManager constantManager = new ConstantManager();
	try {
	    String sqlQuery = "select * from '" + layer.getRecordset().getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.MANZANA_NAME_IN_DB + " = " + constantManager.getConstants().getManzana() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() +";";
	    return layer.getSource().getFeatureIterator(sqlQuery, null);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private Geometry getManzanaGeom() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	FLyrVect manzana = tocLayerManager.getLayerByName(Preferences.MANZANAS_LAYER_NAME);
	IGeometry manzanaGeom = getGeomFromFLyrVect(manzana);
	Geometry manzanaJTSGeom = manzanaGeom.toJTSGeometry();
	return manzanaJTSGeom;
    }

    private IGeometry getGeomFromFLyrVect(FLyrVect layer) {
	ConstantManager constantManager = new ConstantManager();
	try {
	    String sqlQuery = "select * from '" + layer.getRecordset().getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.MANZANA_NAME_IN_DB + " = " + constantManager.getConstants().getManzana() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() +";";
	    IFeatureIterator featureIterator = layer.getSource().getFeatureIterator(sqlQuery, null);
	    return featureIterator.next().getGeometry();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this, "rule_manzana_area_is_distint_all_predios_area");
    }

}
