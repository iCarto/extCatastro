package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

import es.icarto.gvsig.catastro.evaluator.actions.IAction;
import es.icarto.gvsig.catastro.evaluator.actions.UpdateConstructionsFather;
import es.icarto.gvsig.catastro.evaluator.actions.UpdateConstructionsGeom;

public class PredioActionsDivideEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;
    private ArrayList<IGeometry> prediosGeoms = null;

    public PredioActionsDivideEvaluator(ArrayList<IGeometry> geoms) {
	actions = new ArrayList<IAction>();
	messages = new ArrayList<String>();
	prediosGeoms = geoms;
	init();
    }

    private void init() {
	actions.add(new UpdateConstructionsGeom(prediosGeoms));
	actions.add(new UpdateConstructionsFather(prediosGeoms));
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
