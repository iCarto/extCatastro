package es.icarto.gvsig.catastro.actions;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class ManzanaRulesEvaluator {

	IGeometry geometryInserted;
	ArrayList<ITopologicalRule> topologicalRules;
	ArrayList<IBusinessRule> businessRules;

	public ManzanaRulesEvaluator(IGeometry geometryInserted) {
		this.geometryInserted = geometryInserted;
		topologicalRules = new ArrayList<ITopologicalRule>();
		businessRules = new ArrayList<IBusinessRule>();
		init();
	}

	private void init() {
		topologicalRules.add(new CheckManzanaIsOverlapingAnotherOne(geometryInserted));
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
