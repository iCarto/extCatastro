package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import es.icarto.gvsig.catastro.evaluator.rules.IRule;

public class AbstractRulesEvaluator {

    private String errorMessage = null;
    protected ArrayList<IRule> rules;

    public AbstractRulesEvaluator() {
	rules = new ArrayList<IRule>();
    }

    public boolean isOK() {
	for (IRule rule : rules) {
	    if (!rule.isObey()) {
		errorMessage  = rule.getMessage();
		return false;
	    }
	}
	return true;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

}