package es.icarto.gvsig.catastro;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
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

import es.icarto.gvsig.catastro.evaluator.actions.CalculateIDNewPredio;
import es.icarto.gvsig.catastro.evaluator.actions.ManzanaActionsEvaluator;
import es.icarto.gvsig.catastro.evaluator.actions.PredioActionsEvaluator;
import es.icarto.gvsig.catastro.evaluator.rules.ManzanaRulesEvaluator;
import es.icarto.gvsig.catastro.evaluator.rules.PredioRulesEvaluator;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class ActionDispatcherExtension extends Extension implements
	EndGeometryListener {

    private static final int NO_ACTION = -1;
    private final int ACTION_CALCULATE_NEW_PREDIO_ID = 0;
    private final int ACTION_CHECK_RULES_FOR_DIVIDING_PREDIO = 1;
    private static final int ACTION_CHECK_RULES_FOR_NEW_MANZANA = 2;

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
	int action = getAction(layer, cadToolKey, cadTool);
	ToggleEditing te = new ToggleEditing();
	TOCLayerManager tocLayerManager = new TOCLayerManager();

	if (action == ACTION_CALCULATE_NEW_PREDIO_ID) {
	    IRowEdited selectedRow = ((CutPolygonCADTool) cadTool)
		    .getSelectedRow();
	    CalculateIDNewPredio calculator = new CalculateIDNewPredio(
		    (FLyrVect) layer, selectedRow);
	    Value[] values = null;
	    if (calculator.execute()) {
		values = calculator.getAttributes();
	    }
	    ((CutPolygonCADTool) cadTool).setParametrizableValues(values);
	} else if (action == ACTION_CHECK_RULES_FOR_DIVIDING_PREDIO) {
	    ArrayList<IGeometry> geoms = ((CutPolygonCADTool) cadTool)
		    .getGeometriesCreated();
	    PredioRulesEvaluator predioRulesEvaluator = new PredioRulesEvaluator(
		    geoms);
	    if (!predioRulesEvaluator.isOK()) {
		if (tocLayerManager.isPrediosLayerInEdition()) {
		    te.stopEditing(layer, true);
		}
		JOptionPane.showMessageDialog(null, predioRulesEvaluator
			.getErrorMessage(), "Divide predio",
			JOptionPane.WARNING_MESSAGE);
	    } else {
		int option = JOptionPane.showConfirmDialog(null, PluginServices
			.getText(this, "save_predio_confirm"), "Divide predio",
			JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_OPTION,
			null);
		if (option == JOptionPane.OK_OPTION) {
		    PredioActionsEvaluator predioActions = new PredioActionsEvaluator();
		    predioActions.execute();
		}
		if (tocLayerManager.isPrediosLayerInEdition()) {
		    te.stopEditing(layer, true);
		}
	    }
	} else if (action == ACTION_CHECK_RULES_FOR_NEW_MANZANA) {
	    IGeometry insertedGeometry = ((AreaCADTool) cadTool)
		    .getInsertedGeometry();
	    int rowIndex = ((AreaCADTool) cadTool).getVirtualIndex();
	    ManzanaRulesEvaluator manzanaRulesEvaluator = new ManzanaRulesEvaluator(
		    insertedGeometry);
	    if (!manzanaRulesEvaluator.isOK()) {
		if (tocLayerManager.isManzanaLayerInEdition()) {
		    te.stopEditing(layer, true);
		}
		JOptionPane.showMessageDialog(null, manzanaRulesEvaluator
			.getErrorMessage(), "Alta Manzana",
			JOptionPane.WARNING_MESSAGE);
	    } else {
		int option = JOptionPane.showConfirmDialog(null, PluginServices
			.getText(this, "save_manzana_confirm"),
			"Crear Manzana", JOptionPane.YES_NO_OPTION,
			JOptionPane.YES_NO_OPTION, null);
		if (option == JOptionPane.OK_OPTION) {
		    ManzanaActionsEvaluator manzanaActionsEvaluator = new ManzanaActionsEvaluator(
			    (FLyrVect) layer, rowIndex);
		    manzanaActionsEvaluator.execute();
		}
		if (tocLayerManager.isManzanaLayerInEdition()) {
		    te.stopEditing(layer, true);
		}
	    }
	}
    }

    private int getAction(FLayer layer, String cadToolKey, CADTool cadTool) {
	if ((cadToolKey
		.equalsIgnoreCase(CutPolygonCADTool.CUT_END_FIRST_POLYGON))
		&& (cadTool instanceof CutPolygonCADTool)
		&& (layer instanceof FLyrVect)) {
	    return ACTION_CALCULATE_NEW_PREDIO_ID;
	} else if ((cadToolKey.equalsIgnoreCase(CutPolygonCADTool.CUT_END))
		&& (cadTool instanceof CutPolygonCADTool)
		&& (layer instanceof FLyrVect)) {
	    return ACTION_CHECK_RULES_FOR_DIVIDING_PREDIO;
	} else if (cadToolKey.equalsIgnoreCase(AreaCADTool.AREA_ACTION_COMMAND)
		&& (cadTool instanceof AreaCADTool)
		&& (layer instanceof FLyrVect)) {
	    return ACTION_CHECK_RULES_FOR_NEW_MANZANA;
	}
	return NO_ACTION;
    }

}
