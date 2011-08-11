package es.icarto.gvsig.catastro.actions.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class ManzanaRulesEvaluator {

	IGeometry geometryInserted;
	ArrayList<ITopologicalRule> topologicalRules;
	ArrayList<IBusinessRule> businessRules;
	String errorMessage = null;

	public ManzanaRulesEvaluator(IGeometry geometryInserted) {
		this.geometryInserted = geometryInserted;
		topologicalRules = new ArrayList<ITopologicalRule>();
		businessRules = new ArrayList<IBusinessRule>();
		init();
	}

	private void init() {
		topologicalRules.add(new CheckManzanaIsOverlapingAnotherOne(
				geometryInserted));
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
