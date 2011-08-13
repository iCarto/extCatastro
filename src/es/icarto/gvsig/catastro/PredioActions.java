package es.icarto.gvsig.catastro;

import java.util.ArrayList;

import es.icarto.gvsig.catastro.actions.UpdateAreaPredioInDB;
import es.icarto.gvsig.catastro.actions.UpdateConstructionsFather;
import es.icarto.gvsig.catastro.actions.UpdateConstructionsGeom;

public class PredioActions {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;

    public PredioActions(){
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
