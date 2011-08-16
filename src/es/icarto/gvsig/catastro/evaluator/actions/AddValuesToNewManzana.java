package es.icarto.gvsig.catastro.evaluator.actions;

import java.util.ArrayList;
import java.util.Arrays;

import org.gvsig.fmap.core.NewFConverter;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class AddValuesToNewManzana implements IAction {

    int rowIndex;
    FLyrVect layer = null;
    SelectableDataSource recordset;
    ConstantManager constantManager;
    private String[] manzanasIDs;

    public AddValuesToNewManzana(FLyrVect layer, int rowIndex) {
	this.layer = layer;
	this.rowIndex = rowIndex;
    }

    @Override
    public boolean execute() {
	constantManager = new ConstantManager();
	try {
	    recordset = layer.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
	manzanasIDs = getAllManzanasIDInRecordset();
	if (manzanasIDs != null && addValues()) {
	    constantManager.getConstants().setManzana(getNewManzanaID());
	    return true;
	} else {
	    return false;
	}
    }

    private boolean addValues() {
	try {
	    int[] updatePositions = new int[7];
	    String[] updateValues = new String[7];

	    updatePositions[0] = getPaisIndex();
	    updatePositions[1] = getEstadoIndex();
	    updatePositions[2] = getMunicipioIndex();
	    updatePositions[3] = getLimiteIndex();
	    updatePositions[4] = getRegionIndex();
	    updatePositions[5] = getManzanaIndex();
	    updatePositions[6] = getManzanaAreaIndex();

	    updateValues[0] = constantManager.getConstants().getPais();
	    updateValues[1] = constantManager.getConstants().getEstado();
	    updateValues[2] = constantManager.getConstants().getMunicipio();
	    updateValues[3] = constantManager.getConstants().getLimiteMunicipal();
	    updateValues[4] = constantManager.getConstants().getRegion();
	    updateValues[5] = getNewManzanaID();
	    updateValues[6] = Double.toString(getAreaOfNewManzana());

	    ToggleEditing te = new ToggleEditing();
	    te.modifyValues(layer, rowIndex, updatePositions, updateValues);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private double getAreaOfNewManzana() {
	double area = 0;
	try {
	    IGeometry manzanaGeom = layer.getSource().getFeature(rowIndex)
		    .getGeometry();
	    Geometry manzanaJTSGeom = NewFConverter.toJtsGeometry(manzanaGeom);
	    area = manzanaJTSGeom.getArea();
	} catch (ExpansionFileReadException e) {
	    e.printStackTrace();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	System.out.println("AREA: " + area);
	return area;
    }

    private String getNewManzanaID() {
	Arrays.sort(manzanasIDs);
	int biggerManzanaID = Integer
		.parseInt(manzanasIDs[manzanasIDs.length - 1]);
	String newManzanaID = String.format("%1$03d", (biggerManzanaID + 1));
	return newManzanaID;
    }

    private String[] getAllManzanasIDInRecordset() {
	SelectableDataSource originalRecordset;
	ArrayList<String> manzanasID = new ArrayList<String>();
	try {
	    int columnIndex = getManzanaIndex();
	    originalRecordset = layer.getRecordset();
	    String sqlQuery = "select * from '" + layer.getRecordset().getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() + " " +
		    ";";
	    DataSourceFactory dsf = originalRecordset.getDataSourceFactory();
	    DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	    ds.setDataSourceFactory(dsf);
	    SelectableDataSource filteredRecordset = new SelectableDataSource(
		    ds);
	    for (int rowIndex = 0; rowIndex < filteredRecordset.getRowCount(); rowIndex++) {
		manzanasID.add(filteredRecordset.getFieldValue(rowIndex,
			columnIndex).toString());
	    }
	    return manzanasID.toArray(new String[] { "" });
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

    private int getPaisIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.PAIS_NAME_IN_DB);
    }

    private int getEstadoIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.ESTADO_NAME_IN_DB);
    }

    private int getMunicipioIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.MUNICIPIO_NAME_IN_DB);
    }

    private int getLimiteIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.LIMITE_NAME_IN_DB);
    }

    private int getRegionIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.REGION_NAME_IN_DB);
    }

    private int getManzanaIndex() throws ReadDriverException {
	return recordset.getFieldIndexByName(Preferences.MANZANA_NAME_IN_DB);
    }

    private int getManzanaAreaIndex() throws ReadDriverException {
	return recordset
		.getFieldIndexByName(Preferences.MANZANA_AREA_NAME_IN_DB);
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_add_values_to_new_manzana_was_not_performed");
    }
}
