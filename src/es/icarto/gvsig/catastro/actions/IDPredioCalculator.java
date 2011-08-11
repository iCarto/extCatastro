package es.icarto.gvsig.catastro.actions;

import java.util.Arrays;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.utils.Preferences;

public class IDPredioCalculator {

    FLyrVect layer = null;
    IRowEdited selectedRow = null;

    public IDPredioCalculator(FLyrVect l, IRowEdited row) {
	layer = l;
	selectedRow = row;
    }

    public Value[] getAttributes() {
	int numAttr;
	try {
	    numAttr = layer.getRecordset().getFieldCount();
	    Value[] values = new Value[numAttr];
	    values = selectedRow.getAttributes().clone();
	    int predioIDIndex = getPredioIDIndex();
	    values[predioIDIndex] = getNewPredioID();
	    return values;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private Value getNewPredioID() {
	//TODO: get a set of predios taking into account the constants
	String[] prediosID = {"1", "3", "2"};
	Arrays.sort(prediosID);
	return ValueFactory.createValue(prediosID[prediosID.length-1]);
    }

    private int getPredioIDIndex(){
	String[] fieldNames;
	int predioIndex = -1;
	try {
	    fieldNames = layer.getRecordset().getFieldNames();
	    for (int i=0; i<fieldNames.length; i++){
		if (fieldNames[i].equalsIgnoreCase(Preferences.PREDIO_NAME_IN_DB)){
		    predioIndex = i;
		    return predioIndex;
		}
	    }
	    return predioIndex;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return predioIndex;
	}
    }

}