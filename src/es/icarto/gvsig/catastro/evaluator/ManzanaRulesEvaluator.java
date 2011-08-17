package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

import es.icarto.gvsig.catastro.evaluator.rules.CheckGeometryIsOverlapingAnotherOne;
import es.icarto.gvsig.catastro.evaluator.rules.CheckManzanaIsWithinOneRegion;
import es.icarto.gvsig.catastro.evaluator.rules.IRule;
import es.icarto.gvsig.catastro.utils.Preferences;

public class ManzanaRulesEvaluator extends AbstractEvaluator {

    IGeometry insertedGeometry;

    public ManzanaRulesEvaluator(IGeometry insertedGeometry) {
	this.insertedGeometry = insertedGeometry;
	rules = new ArrayList<IRule>();
	init();
    }

    private void init() {
	rules.add(new CheckGeometryIsOverlapingAnotherOne(insertedGeometry,
		Preferences.MANZANAS_LAYER_NAME));
	rules.add(new CheckManzanaIsWithinOneRegion(insertedGeometry));
    }

}
