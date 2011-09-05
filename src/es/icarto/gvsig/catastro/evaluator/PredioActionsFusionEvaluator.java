package es.icarto.gvsig.catastro.evaluator;

import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FLayer;

import es.icarto.gvsig.catastro.evaluator.AbstractActionsEvaluator;
import es.icarto.gvsig.catastro.evaluator.actions.UpdatePredioIDInConstrucciones;

public class PredioActionsFusionEvaluator extends AbstractActionsEvaluator {

    private IFeature predio = null;
    private FLayer construccionesLayer = null;

    public PredioActionsFusionEvaluator(FLayer construccionesLayer,
	    IFeature predioFusioned) {
	this.predio = predioFusioned;
	this.construccionesLayer = construccionesLayer;
	init();
    }

    private void init() {
	actions.add(new UpdatePredioIDInConstrucciones(construccionesLayer,
		predio));
    }
}
