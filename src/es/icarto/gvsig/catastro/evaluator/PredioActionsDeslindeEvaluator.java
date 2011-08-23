package es.icarto.gvsig.catastro.evaluator;

import com.iver.cit.gvsig.fmap.core.IGeometry;

import es.icarto.gvsig.catastro.evaluator.actions.PredioDeslindeWithManzana;

public class PredioActionsDeslindeEvaluator extends AbstractActionsEvaluator {

    private final IGeometry redigitalizedPredioGeom;

    public PredioActionsDeslindeEvaluator(IGeometry redigitalizedPredioGeom) {
	this.redigitalizedPredioGeom = redigitalizedPredioGeom;
	init();
    }

    private void init() {
	actions.add(new PredioDeslindeWithManzana(redigitalizedPredioGeom));
    }

}
