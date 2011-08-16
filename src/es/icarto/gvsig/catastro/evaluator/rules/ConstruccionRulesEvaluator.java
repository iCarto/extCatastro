package es.icarto.gvsig.catastro.evaluator.rules;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;

import es.icarto.gvsig.catastro.utils.Preferences;

public class ConstruccionRulesEvaluator extends AbstractEvaluator {

    IGeometry insertedGeometry;

    public ConstruccionRulesEvaluator(IGeometry insertedGeometry) {
	this.insertedGeometry = insertedGeometry;
	rules = new ArrayList<IRule>();
	init();
    }

    private void init() {
	rules.add(new CheckGeometryIsOverlapingAnotherOne(insertedGeometry,
		Preferences.CONSTRUCCIONES_LAYER_NAME));
	rules.add(new CheckConstruccionIsWithinOnePredio(insertedGeometry));
    }

}
