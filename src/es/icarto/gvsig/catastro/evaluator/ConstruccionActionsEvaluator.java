package es.icarto.gvsig.catastro.evaluator;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.evaluator.actions.ConstruccionCalculateValues;

public class ConstruccionActionsEvaluator extends AbstractActionsEvaluator {

    private FLyrVect layer = null;
    private final int rowIndex;

    public ConstruccionActionsEvaluator(FLyrVect layer, int rowIndex) {
	super();
	this.layer = layer;
	this.rowIndex = rowIndex;
	init();
    }

    public void init() {
	actions.add(new ConstruccionCalculateValues(layer, rowIndex));
    }

}
