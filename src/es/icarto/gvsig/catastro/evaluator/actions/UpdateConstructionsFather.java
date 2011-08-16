package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class UpdateConstructionsFather implements IAction {

    private ArrayList<IGeometry> predios = null;
    private FLyrVect construccionesLayer = null;

    public UpdateConstructionsFather(ArrayList<IGeometry> prediosGeoms) {
	this.predios = prediosGeoms;
	construccionesLayer = getConstrucciones();
    }

    private FLyrVect getConstrucciones() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
    }

    @Override
    public boolean execute() {
	if(updateIDPredioForConstrucciones()){
	    return true;
	}
	return false;
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this, "action_update_id_construcciones_not_possible");
    }

    private boolean updateIDPredioForConstrucciones(){
	ConstantManager constantManager = new ConstantManager();
	Geometry newPredio = predios.get(1).toJTSGeometry().buffer(0.5);
	try {
	    String sqlQuery = "select * from '" + construccionesLayer.getRecordset().getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() + " " +
		    " and " + Preferences.MANZANA_NAME_IN_DB + " = " + constantManager.getConstants().getManzana() + " "+
		    " and " + Preferences.PREDIO_NAME_IN_DB + " = " + constantManager.getConstants().getPredio() + " "+
		    ";";
	    IFeatureIterator featureIterator = construccionesLayer.getSource().getFeatureIterator(sqlQuery, null);
	    while(featureIterator.hasNext()){
		IFeature feature = featureIterator.next();
		Geometry construccionJTS = feature.getGeometry().toJTSGeometry();
		if(construccionJTS.coveredBy(newPredio)){
		    updateID(feature.getAttributes());
		}
	    }
	    return true;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    private void updateID(Value[] values) {
	System.out.println(" -- Updated ID for construccion ID:" + values[8]);
    }
}
