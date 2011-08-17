package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;
import java.util.Arrays;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.catastro.utils.Preferences;

public class ConstruccionCalculateNewID implements IAction {

    private FLyrVect construccionesLayer = null;
    private Integer[] construccionesIDs;

    public ConstruccionCalculateNewID(FLyrVect l) {
	construccionesLayer = l;
    }

    public boolean execute() {
	construccionesIDs = getAllConstruccionesIDInRecordset();
	if (construccionesIDs != null) {
	    return true;
	}
	return false;
    }

    public Value getNewConstruccionID() {
	Arrays.sort(construccionesIDs);
	int biggerConstruccionID = construccionesIDs[construccionesIDs.length - 1];
	String newConstruccionID = String.format("%1$03d",
		(biggerConstruccionID + 1));
	return ValueFactory.createValue(Integer.parseInt(newConstruccionID));
    }

    private Integer[] getAllConstruccionesIDInRecordset() {
	SelectableDataSource recordset;
	int columnIndex = getConstruccionIDIndex();
	ArrayList<Integer> construccionesID = new ArrayList<Integer>();
	try {
	    recordset = construccionesLayer.getRecordset();
	    for (int rowIndex = 0; rowIndex < recordset.getRowCount(); rowIndex++) {
		construccionesID.add(Integer.parseInt(recordset.getFieldValue(
			rowIndex, columnIndex).toString()));
	    }
	    return construccionesID.toArray(new Integer[] { 0 });
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private int getConstruccionIDIndex() {
	String[] fieldNames;
	int construccionIndex = -1;
	try {
	    fieldNames = construccionesLayer.getRecordset().getFieldNames();
	    for (int i = 0; i < fieldNames.length; i++) {
		if (fieldNames[i]
			.equalsIgnoreCase(Preferences.CONSTRUCCIONES_NAME_IN_DB)) {
		    construccionIndex = i;
		    return construccionIndex;
		}
	    }
	    return construccionIndex;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return construccionIndex;
	}
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_update_id_new_construccion_was_not_performed");
    }

}