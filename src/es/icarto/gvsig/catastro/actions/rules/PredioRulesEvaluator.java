package es.icarto.gvsig.catastro.actions.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class PredioRulesEvaluator {

    ArrayList<IGeometry> geoms;
    ArrayList<ITopologicalRule> topologicalRules;
    ArrayList<IBusinessRule> businessRules;

    public PredioRulesEvaluator(ArrayList<IGeometry> geoms) {
	this.geoms = geoms;
	topologicalRules = new ArrayList<ITopologicalRule>();
	businessRules = new ArrayList<IBusinessRule>();
	init();
    }

    private void init() {
	topologicalRules.add(new CheckPredioIsWithinOneManzana(geoms));
	topologicalRules.add(new CheckAllAreaPrediosEqualsAreaManzana());
	businessRules.add(new UpdateConstructionsGeom());
	businessRules.add(new UpdateConstructionsFather());
	businessRules.add(new UpdateAreaPredioInDB());
    }

    public boolean isOK() {
	for (ITopologicalRule topologicalRule : topologicalRules) {
	    if (!topologicalRule.isObey()) {
		return false;
	    }
	}
	for (IBusinessRule businessRule : businessRules) {
	    if (!businessRule.launchRule()) {
		return false;
	    }
	}
	return true;
    }

}