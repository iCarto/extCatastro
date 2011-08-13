package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;


public class PredioActionsEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;

    public PredioActionsEvaluator(){
	actions = new ArrayList<IAction>();
	messages = new ArrayList<String>();
	init();
    }

    private void init(){
	actions.add(new UpdateAreaPredioInDB());
	actions.add(new UpdateConstructionsFather());
	actions.add(new UpdateConstructionsGeom());
    }

    public ArrayList<String> execute(){
	for (IAction action: actions){
	    if(!action.execute()){
		messages.add(action.getMessage());
	    }
	}
	return messages;
    }

}
