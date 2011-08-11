package es.icarto.gvsig.catastro.actions;

import java.util.ArrayList;

public class ManzanaRulesEvaluator {

	ArrayList<ITopologicalRule> topologicalRules;
	ArrayList<IBusinessRule> businessRules;

	public ManzanaRulesEvaluator() {
		topologicalRules = new ArrayList<ITopologicalRule>();
		businessRules = new ArrayList<IBusinessRule>();
		init();
	}

	private void init() {
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
