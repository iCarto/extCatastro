package es.icarto.gvsig.catastro.rules;

import java.util.ArrayList;

public class AbstractEvaluator {

    private String errorMessage = null;
    protected ArrayList<IRule> rules;

    public AbstractEvaluator() {
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