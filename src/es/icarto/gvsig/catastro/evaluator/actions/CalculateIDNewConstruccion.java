package es.icarto.gvsig.catastro.evaluator.actions;

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
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;

public class CalculateIDNewConstruccion implements IAction {

    FLyrVect construccionesLayer = null;
    ConstantManager constantManager = null;
    private Integer[] construccionesIDs;

    public CalculateIDNewConstruccion(FLyrVect l) {
	construccionesLayer = l;
	constantManager = new ConstantManager();
    }

    public boolean execute() {
	construccionesIDs = getAllConstruccionesIDInRecordset();
	if(construccionesIDs != null){
	    return true;
	}
	return false;
    }

    public Value getNewConstruccionID(){
	Arrays.sort(construccionesIDs);
	int biggerConstruccionID = construccionesIDs[construccionesIDs.length-1];
	String newConstruccionID = String.format("%1$03d", (biggerConstruccionID+1));
	return ValueFactory.createValue(newConstruccionID);
    }

    private Integer[] getAllConstruccionesIDInRecordset() {
	SelectableDataSource originalRecordset;
	int columnIndex = getConstruccionIDIndex();
	ArrayList<Integer> construccionesID = new ArrayList<Integer>();
	try {
	    originalRecordset = construccionesLayer.getRecordset();
	    String sqlQuery = "select * from '" + originalRecordset.getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.MANZANA_NAME_IN_DB + " = " + constantManager.getConstants().getManzana() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() + "" +
		    " and " + Preferences.PREDIO_NAME_IN_DB + " = " + constantManager.getConstants().getPredio() +";";
	    DataSourceFactory dsf = originalRecordset.getDataSourceFactory();
	    DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	    ds.setDataSourceFactory(dsf);
	    SelectableDataSource filteredRecordset= new SelectableDataSource(ds);
	    for (int rowIndex=0; rowIndex<filteredRecordset.getRowCount(); rowIndex++){
		construccionesID.add(Integer.parseInt(filteredRecordset.getFieldValue(rowIndex, columnIndex).toString()));
	    }
	    return construccionesID.toArray(new Integer[]{0});
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

    private int getConstruccionIDIndex(){
	String[] fieldNames;
	int construccionIndex = -1;
	try {
	    fieldNames = construccionesLayer.getRecordset().getFieldNames();
	    for (int i=0; i<fieldNames.length; i++){
		if (fieldNames[i].equalsIgnoreCase(Preferences.CONSTRUCCIONES_NAME_IN_DB)){
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
	return PluginServices.getText(this, "action_update_id_new_construccion_was_not_performed");
    }

}