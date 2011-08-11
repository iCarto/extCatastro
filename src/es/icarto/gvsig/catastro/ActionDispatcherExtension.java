package es.icarto.gvsig.catastro;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.tools.AreaCADTool;
import com.iver.cit.gvsig.gui.cad.tools.CutPolygonCADTool;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.icarto.gvsig.catastro.actions.IDPredioCalculator;
import es.icarto.gvsig.catastro.actions.ManzanaRulesEvaluator;
import es.icarto.gvsig.catastro.actions.PredioRulesEvaluator;

public class ActionDispatcherExtension extends Extension implements
		EndGeometryListener {

	@Override
	public void initialize() {
		CADListenerManager.removeEndGeometryListener("catastro");
		CADListenerManager.addEndGeometryListener("catastro", this);
	}

	@Override
	public void execute(String actionCommand) {
		// nothing to do
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void endGeometry(FLayer layer, String cadToolKey) {
		CADTool cadTool = CADExtension.getCADTool();
		if (isDividingPredio(layer, cadToolKey, cadTool)) {
			IRowEdited selectedRow = ((CutPolygonCADTool) cadTool)
					.getSelectedRow();
			IDPredioCalculator newPredio = new IDPredioCalculator(
					(FLyrVect) layer, selectedRow);
			Value[] values = newPredio.getAttributes();
			((CutPolygonCADTool) cadTool).setParametrizableValues(values);
		} else if (isDividingPredioEnded(layer, cadToolKey, cadTool)) {
			IRowEdited selectedRow = ((CutPolygonCADTool) cadTool)
					.getSelectedRow();
			PredioRulesEvaluator predioRulesEvaluator = new PredioRulesEvaluator(
					selectedRow);
			if (predioRulesEvaluator.isOK()) {
				// TODO: launch padron form for the user to update
				System.out.println(" -------- Launch form");
			}
		} else if (cadToolKey.equalsIgnoreCase(AreaCADTool.AREA_ACTION_COMMAND)
				&& (cadTool instanceof AreaCADTool)
				&& (layer instanceof FLyrVect)) {
			// CreatePredioWhenAddNewManzana createPredio = new
			// CreatePredioWhenAddNewManzana(
			// (FLyrVect) layer);
			IGeometry insertedGeometry = ((AreaCADTool) cadTool)
					.getInsertedGeometry();
			ManzanaRulesEvaluator manzanaRulesEvaluator = new ManzanaRulesEvaluator(
					insertedGeometry);
			if (!manzanaRulesEvaluator.isOK()) {
				System.out.println("====No se cumple la regla");
			} else {
				System.out.println("=====Se cumple la regla");
			}
		}
	}

	private boolean isDividingPredioEnded(FLayer layer, String cadToolKey,
			CADTool cadTool) {
		return (cadToolKey.equalsIgnoreCase(CutPolygonCADTool.CUT_END))
				&& (cadTool instanceof CutPolygonCADTool)
				&& (layer instanceof FLyrVect);
	}

	private boolean isDividingPredio(FLayer layer, String cadToolKey,
			CADTool cadTool) {
		return (cadToolKey
				.equalsIgnoreCase(CutPolygonCADTool.CUT_END_FIRST_POLYGON))
				&& (cadTool instanceof CutPolygonCADTool)
				&& (layer instanceof FLyrVect);
	}

}
