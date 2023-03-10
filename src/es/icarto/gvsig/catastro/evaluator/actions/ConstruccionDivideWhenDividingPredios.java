package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.catastro.utils.FeaturesRetrieverFromIntersection;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class ConstruccionDivideWhenDividingPredios implements IAction {

    private ArrayList<IGeometry> predios;
    private FLyrVect construccionesLayer;
    private FLyrVect prediosLayer;
    private int idNewPredio = -1;
    private ToggleEditing te;
    private ArrayList<IFeature> construccionesAffected;

    public ConstruccionDivideWhenDividingPredios(ArrayList<IGeometry> predios,
	    int idNewPredio) {
	this.predios = predios;
	this.idNewPredio = idNewPredio;
	init();
    }

    private void init() {
	TOCLayerManager toc = new TOCLayerManager();
	te = new ToggleEditing();
	this.prediosLayer = toc.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
	this.construccionesLayer = getConstrucciones();
	construccionesAffected = getConstruccionesAffected();
    }

    @Override
    public boolean execute() {
	prediosLayer.setActive(false);
	construccionesLayer.setActive(true);
	te.startEditing(construccionesLayer);
	for (IFeature construccion : construccionesAffected) {
	    if (!save(construccion)) {
		return false;
	    }
	}
	return true;
    }

    private FLyrVect getConstrucciones() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	return tocLayerManager
		.getLayerByName(Preferences.CONSTRUCCIONES_LAYER_NAME);
    }

    private boolean save(IFeature construccion) {
	ConstruccionDivideFeaturesRetriever construccionesCutter = new ConstruccionDivideFeaturesRetriever(
		construccion, predios, idNewPredio);
	construccionesCutter.execute();
	ArrayList<IFeature> featuresToModify = construccionesCutter
		.getFeaturesToModify();
	ArrayList<IFeature> featuresToAdd = construccionesCutter
		.getFeaturesToAdd();
	for (IFeature feature : featuresToModify) {
	    int row = getIndexOfFeatureSelected(feature);
	    SelectableDataSource sds;
	    try {
		sds = construccionesLayer.getRecordset();
		int col = sds.getFieldIndexByName(Preferences.GID_IN_DB);
		feature.setID(construccionesLayer.getRecordset()
			.getFieldValue(row, col).toString());
		te.modifyFeature(row + 1, feature, "_cutConstruccionesAffected");
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		return false;
	    }
	}
	for (IFeature feature : featuresToAdd) {
	    te.addGeometryWithParametrizedValues(feature.getGeometry(),
		    feature.getAttributes(), "_cutConstrucciones");
	}
	return true;
    }

    private int getIndexOfFeatureSelected(IFeature feature) {
	try {
	    SelectableDataSource sds = construccionesLayer.getRecordset();
	    int col = sds.getFieldIndexByName(Preferences.GID_IN_DB);
	    for (int row = 0; row < sds.getRowCount(); row++) {
		if (sds.getFieldValue(row, col).toString()
			.equalsIgnoreCase(feature.getAttribute(col).toString())) {
		    return row;
		}
	    }
	    return -1;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return -1;
	}
    }

    private ArrayList<IFeature> getConstruccionesAffected() {
	FeaturesRetrieverFromIntersection featuresRetriever = new FeaturesRetrieverFromIntersection(
		construccionesLayer, predios);
	return featuresRetriever.getFeatures();
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_cut_constructions_not_possible");
    }

}
