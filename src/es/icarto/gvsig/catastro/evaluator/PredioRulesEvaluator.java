package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

import es.icarto.gvsig.catastro.evaluator.rules.CheckAllAreaPrediosEqualsAreaManzana;
import es.icarto.gvsig.catastro.evaluator.rules.CheckPredioIsWithinOneManzana;

public class PredioRulesEvaluator extends AbstractEvaluator {

    ArrayList<IGeometry> geoms;

    public PredioRulesEvaluator(ArrayList<IGeometry> geoms) {
	super();
	this.geoms = geoms;
	init();
    }

    private void init() {
	rules.add(new CheckPredioIsWithinOneManzana(geoms));
	rules.add(new CheckAllAreaPrediosEqualsAreaManzana());
    }

}