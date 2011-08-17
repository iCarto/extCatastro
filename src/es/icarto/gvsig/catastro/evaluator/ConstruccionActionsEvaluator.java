package es.icarto.gvsig.catastro.evaluator;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.evaluator.actions.ConstruccionNewCalculateValues;

public class ConstruccionActionsEvaluator extends AbstractActionsEvaluator {

    private FLyrVect construccionesLayer = null;
    private final int rowIndex;

    public ConstruccionActionsEvaluator(FLyrVect layer, int rowIndex) {
	super();
	this.construccionesLayer = layer;
	this.rowIndex = rowIndex;
	init();
    }

    public void init() {
	actions.add(new ConstruccionNewCalculateValues(construccionesLayer, rowIndex));
    }

}
