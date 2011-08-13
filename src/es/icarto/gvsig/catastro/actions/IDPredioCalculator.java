package es.icarto.gvsig.catastro.actions;

import java.util.ArrayList;
import java.util.Arrays;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.catastro.IAction;
import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;

public class IDPredioCalculator implements IAction {

    FLyrVect layer = null;
    IRowEdited selectedRow = null;
    ConstantManager constantManager = null;

    public IDPredioCalculator(FLyrVect l, IRowEdited row) {
	layer = l;
	selectedRow = row;
	constantManager = new ConstantManager();
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
	String[] prediosID = getAllPrediosIDInRecordset();
	Arrays.sort(prediosID);
	int biggerPredioID = Integer.parseInt(prediosID[prediosID.length-1]);
	String newPredioID = String.format("%1$03d", (biggerPredioID+1));
	return ValueFactory.createValue(newPredioID);
    }

    private String[] getAllPrediosIDInRecordset() {
	SelectableDataSource originalRecordset;
	int columnIndex = getPredioIDIndex();
	ArrayList<String> prediosID = new ArrayList<String>();
	try {
	    originalRecordset = layer.getRecordset();
	    String sqlQuery = "select * from " + originalRecordset.getName() +
		    " where " + Preferences.MANZANA_NAME_IN_DB + " ='" + constantManager.getConstants().getManzana() + "' "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = '" + constantManager.getConstants().getRegion() +"';";
	    DataSourceFactory dsf = originalRecordset.getDataSourceFactory();
	    DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	    ds.setDataSourceFactory(dsf);
	    SelectableDataSource filteredRecordset= new SelectableDataSource(ds);
	    for (int rowIndex=0; rowIndex<filteredRecordset.getRowCount(); rowIndex++){
		prediosID.add(filteredRecordset.getFieldValue(rowIndex, columnIndex).toString());
	    }
	    return prediosID.toArray(new String[]{""});
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	} catch (DriverLoadException e) {
	    e.printStackTrace();
	    return null;
	} catch (ParseException e) {
	    e.printStackTrace();
	    return null;
	} catch (SemanticException e) {
	    e.printStackTrace();
	    return null;
	} catch (EvaluationException e) {
	    e.printStackTrace();
	    return null;
	}
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

    @Override
    public boolean execute() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getMessage() {
	// TODO Auto-generated method stub
	return null;
    }

}