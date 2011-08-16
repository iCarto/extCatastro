package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;


public class PredioActionsEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;
    private ArrayList<IGeometry> prediosGeoms = null;

    public PredioActionsEvaluator(ArrayList<IGeometry> geoms){
	actions = new ArrayList<IAction>();
	messages = new ArrayList<String>();
	prediosGeoms = geoms;
	init();
    }

    private void init(){
	actions.add(new UpdateConstructionsGeom(prediosGeoms));
	//	actions.add(new UpdateConstructionsFather(prediosGeoms));
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
