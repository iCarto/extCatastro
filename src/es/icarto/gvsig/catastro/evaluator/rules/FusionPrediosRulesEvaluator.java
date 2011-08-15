package es.icarto.gvsig.catastro.evaluator.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public class FusionPrediosRulesEvaluator extends AbstractEvaluator {
    
    ArrayList<IGeometry> geoms;
    
    public FusionPrediosRulesEvaluator(ArrayList<IGeometry> geoms) {
	super();
	this.geoms = geoms;
	init();
    }
    
    private void init() {
	rules.add(new CheckPredioIsWithinOneManzana(geoms));
    }
}
