package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.constants.Constants;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class AddValuesToNewManzana implements IAction {

    int rowIndex;
    FLyrVect layer = null;
    SelectableDataSource recordset;

    public AddValuesToNewManzana(FLyrVect layer, int rowIndex) {
	this.layer = layer;
	this.rowIndex = rowIndex;
	try {
	    recordset = layer.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean execute() {
	addValues();
	return true;
    }

    private void addValues() {
	try {
	    int[] updatePositions = new int[recordset.getFieldCount()];
	    String[] updateValues = new String[recordset.getFieldCount()];

	    updatePositions[0] = getEstadoIndex();
	    updatePositions[1] = getMunicipioIndex();
	    updatePositions[2] = getLimiteIndex();
	    updatePositions[3] = getRegionIndex();

	    ConstantManager constantManager = new ConstantManager();
	    Constants constants = constantManager.getConstants();

	    updateValues[0] = Preferences.ESTADO;
	    updateValues[1] = Preferences.MUNICIPIO;
	    updateValues[2] = Preferences.LIMITE;
	    updateValues[3] = constants.getRegion();

	    ToggleEditing te = new ToggleEditing();
	    te.modifyValues(layer, rowIndex, updatePositions, updateValues);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
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

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_add_values_to_new_manzana_was_not_performed");
    }
}
