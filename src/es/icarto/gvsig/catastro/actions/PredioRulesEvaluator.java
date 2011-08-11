package es.icarto.gvsig.catastro.actions;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.edition.IRowEdited;

public class PredioRulesEvaluator {

	IRowEdited selectedRow;
	ArrayList<ITopologicalRule> topologicalRules;
	ArrayList<IBusinessRule> businessRules;

	public PredioRulesEvaluator(IRowEdited selectedRow) {
		this.selectedRow = selectedRow;
		topologicalRules = new ArrayList<ITopologicalRule>();
		businessRules = new ArrayList<IBusinessRule>();
		init();
	}

	private void init() {
		topologicalRules.add(new CheckPredioIsWithinOneManzana(selectedRow));
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