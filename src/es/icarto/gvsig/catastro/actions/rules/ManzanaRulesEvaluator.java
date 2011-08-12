package es.icarto.gvsig.catastro.actions.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class ManzanaRulesEvaluator {

	IGeometry insertedGeometry;
	ArrayList<ITopologicalRule> topologicalRules;
	ArrayList<IBusinessRule> businessRules;
	String errorMessage = null;

	public ManzanaRulesEvaluator(IGeometry insertedGeometry) {
		this.insertedGeometry = insertedGeometry;
		topologicalRules = new ArrayList<ITopologicalRule>();
		businessRules = new ArrayList<IBusinessRule>();
		init();
	}

	private void init() {
		topologicalRules.add(new CheckManzanaIsOverlapingAnotherOne(
				insertedGeometry));
		topologicalRules
				.add(new CheckManzanaIsWithinOneRegion(insertedGeometry));
	}

	public boolean isOK() {
		for (ITopologicalRule topologicalRule : topologicalRules) {
			if (!topologicalRule.isObey()) {
				errorMessage = topologicalRule.getMessage();
				return false;
			}
		}
		return true;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
