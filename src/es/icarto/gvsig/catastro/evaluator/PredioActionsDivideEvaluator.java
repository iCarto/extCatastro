package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;
import es.icarto.gvsig.catastro.evaluator.actions.IAction;

public class PredioActionsDivideEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;
    private ArrayList<IGeometry> prediosGeoms = null;
    private int idNewPredio = -1;

    public PredioActionsDivideEvaluator(ArrayList<IGeometry> geoms,
	    int idNewPredio) {
	this.prediosGeoms = geoms;
	this.idNewPredio = idNewPredio;
	init();
    }

    private void init() {
	this.actions = new ArrayList<IAction>();
	this.messages = new ArrayList<String>();
	actions.add(new UpdateConstruccionesAffected(prediosGeoms, idNewPredio));
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
