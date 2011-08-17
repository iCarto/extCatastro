package es.icarto.gvsig.catastro.evaluator;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.evaluator.actions.ManzanaCalculateValues;
import es.icarto.gvsig.catastro.evaluator.actions.PredioCreateNewWhenAddNewManzana;

public class ManzanaActionsEvaluator extends AbstractActionsEvaluator {

    private FLyrVect layer = null;
    private final int rowIndex;

    public ManzanaActionsEvaluator(FLyrVect layer, int rowIndex) {
	super();
	this.layer = layer;
	this.rowIndex = rowIndex;
	init();
    }

    public void init() {
	actions.add(new ManzanaCalculateValues(layer, rowIndex));
	actions.add(new PredioCreateNewWhenAddNewManzana(layer));
    }

}
