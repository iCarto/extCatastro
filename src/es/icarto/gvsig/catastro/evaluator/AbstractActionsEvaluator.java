package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import es.icarto.gvsig.catastro.evaluator.actions.IAction;

public class AbstractActionsEvaluator {

    private ArrayList<String> messages = null;
    protected ArrayList<IAction> actions;

    public AbstractActionsEvaluator() {
	actions = new ArrayList<IAction>();
	messages = new ArrayList<String>();
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