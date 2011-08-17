package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.evaluator.actions.IAction;

public class PredioActionsDivideEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;
    private ArrayList<IGeometry> prediosGeoms = null;
    private int idNewPredio = -1;
    private FLyrVect prediosLayer;

    public PredioActionsDivideEvaluator(FLyrVect layer,
	    ArrayList<IGeometry> geoms, int idNewPredio) {
	this.actions = new ArrayList<IAction>();
	this.messages = new ArrayList<String>();
	this.prediosGeoms = geoms;
	this.idNewPredio = idNewPredio;
	this.prediosLayer = layer;
	init();
    }

    private void init() {
	actions.add(new UpdateConstruccionesAffected(prediosLayer,
		prediosGeoms, idNewPredio));
    }

    public ArrayList<String> execute() {
	for (IAction action : actions) {
	    if (!action.execute()) {
		messages.add(action.getMessage());
	    }
	}
	return messages;
    }

}
