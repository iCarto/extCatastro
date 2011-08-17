package es.icarto.gvsig.catastro.evaluator;

import java.util.ArrayList;

import com.iver.cit.gvsig.fmap.core.IGeometry;
import es.icarto.gvsig.catastro.evaluator.actions.ConstruccionDivideWhenDividingPredios;

public class PredioActionsDivideEvaluator extends AbstractActionsEvaluator {

    private ArrayList<IGeometry> prediosGeoms = null;
    private int idNewPredio = -1;

    public PredioActionsDivideEvaluator(ArrayList<IGeometry> geoms,
	    int idNewPredio) {
	this.prediosGeoms = geoms;
	this.idNewPredio = idNewPredio;
	init();
    }

    private void init() {
	actions.add(new ConstruccionDivideWhenDividingPredios(prediosGeoms,
		idNewPredio));
    }

}
