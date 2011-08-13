package es.icarto.gvsig.catastro;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.evaluator.actions.AddValuesToNewManzana;
import es.icarto.gvsig.catastro.evaluator.actions.CreatePredioWhenAddNewManzana;
import es.icarto.gvsig.catastro.evaluator.actions.IAction;

public class ManzanaActionsEvaluator {

    private ArrayList<IAction> actions = null;
    private ArrayList<String> messages = null;
    private FLyrVect layer = null;
    private final int rowIndex;

    public ManzanaActionsEvaluator(FLyrVect layer, int rowIndex) {
	actions = new ArrayList<IAction>();
	messages = new ArrayList<String>();
	this.layer = layer;
	this.rowIndex = rowIndex;
	init();
    }

    public void init() {
	actions.add(new AddValuesToNewManzana(layer, rowIndex));
	actions.add(new CreatePredioWhenAddNewManzana(layer));
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
