package es.icarto.gvsig.catastro.evaluator.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class ManzanaRulesEvaluator extends AbstractEvaluator {

    IGeometry insertedGeometry;

    public ManzanaRulesEvaluator(IGeometry insertedGeometry) {
	this.insertedGeometry = insertedGeometry;
	rules = new ArrayList<IRule>();
	init();
    }

    private void init() {
	rules.add(new CheckManzanaIsOverlapingAnotherOne(
		insertedGeometry));
	rules.add(new CheckManzanaIsWithinOneRegion(
		insertedGeometry));
    }

}
