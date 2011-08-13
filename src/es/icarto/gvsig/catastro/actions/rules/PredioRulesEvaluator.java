package es.icarto.gvsig.catastro.actions.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class PredioRulesEvaluator {

    ArrayList<IGeometry> geoms;
    ArrayList<ITopologicalRule> topologicalRules;

    public PredioRulesEvaluator(ArrayList<IGeometry> geoms) {
	this.geoms = geoms;
	topologicalRules = new ArrayList<ITopologicalRule>();
	init();
    }

    private void init() {
	topologicalRules.add(new CheckPredioIsWithinOneManzana(geoms));
	topologicalRules.add(new CheckAllAreaPrediosEqualsAreaManzana());
	topologicalRules.add(new UpdateConstructionsGeom());
	topologicalRules.add(new UpdateConstructionsFather());
	topologicalRules.add(new UpdateAreaPredioInDB());
    }

    public boolean isOK() {
	for (ITopologicalRule topologicalRule : topologicalRules) {
	    if (!topologicalRule.isObey()) {
		return false;
	    }
	}
	return true;
    }

}