package es.icarto.gvsig.catastro.evaluator.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class ConstruccionNewCalculateValues implements IAction {

    int rowIndex;
    FLyrVect construccionesLayer = null;
    SelectableDataSource recordset;
    ConstantManager constantManager;
    private Integer[] construccionesIDs;

    public ConstruccionNewCalculateValues(FLyrVect layer, int rowIndex) {
	this.construccionesLayer = layer;
	this.rowIndex = rowIndex;
    }

    @Override
    public boolean execute() {
	constantManager = new ConstantManager();
	try {
	    recordset = construccionesLayer.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
	if (addValues(rowIndex, construccionesLayer)) {
	    return true;
	} else {
	    return false;
	}
    }

    private boolean addValues(int rowIndex, FLyrVect layer) {
	try {
	    ConstantManager constantManager = new ConstantManager();
	    int numFields = layer.getRecordset().getFieldCount();
	    int[] positions = new int[11];
	    String[] construccionValues = new String[11];
	    for (int j = 0; j < positions.length; j++) {
		for (int i = j; i < numFields; i++) {
		    if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.PAIS_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getPais();
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.ESTADO_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getEstado();
			positions[j] = i;
			break;
		    } else if (layer
			    .getRecordset()
			    .getFieldName(i)
			    .compareToIgnoreCase(
				    Preferences.MUNICIPIO_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getMunicipio();
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.REGION_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getRegion();
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.LIMITE_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getLimiteMunicipal();
			positions[j] = i;
			break;
		    } else if (layer
			    .getRecordset()
			    .getFieldName(i)
			    .compareToIgnoreCase(Preferences.MANZANA_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getManzana();
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.PREDIO_NAME_IN_DB) == 0) {
			construccionValues[j] = constantManager.getConstants()
				.getPredio();
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.CATMETADATO_IN_DB) == 0) {
			construccionValues[j] = "1";
			positions[j] = i;
			break;
		    } else if (layer
			    .getRecordset()
			    .getFieldName(i)
			    .compareToIgnoreCase(
				    Preferences.CONSTRUCCIONES_NAME_IN_DB) == 0) {
			construccionValues[j] = getNewIDForConstrucciones();
			positions[j] = i;
			break;
		    } else if (layer
			    .getRecordset()
			    .getFieldName(i)
			    .compareToIgnoreCase(
				    Preferences.CONSTRUCCIONES_AREA_NAME_IN_DB) == 0) {
			construccionValues[j] = Double
				.toString(getAreaOfNewConstruccion());
			positions[j] = i;
			break;
		    } else if (layer.getRecordset().getFieldName(i)
			    .compareToIgnoreCase(Preferences.GID_IN_DB) == 0) {
			construccionValues[j] = "";
			positions[j] = i;
			break;
		    }
		}
	    }
	    ToggleEditing te = new ToggleEditing();
	    te.modifyValues(layer, rowIndex, positions, construccionValues);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    private String getNewIDForConstrucciones() {
	ConstruccionCalculateNewID calculateID = new ConstruccionCalculateNewID(
		construccionesLayer);
	calculateID.execute();
	return calculateID.getNewConstruccionID().toString();
    }

    private double getAreaOfNewConstruccion() {
	double area = 0;
	try {
	    IGeometry construccionGeom = construccionesLayer.getSource()
		    .getFeature(rowIndex).getGeometry();
	    Geometry construccionJTSGeom = construccionGeom.toJTSGeometry();
	    area = construccionJTSGeom.getArea();
	} catch (ExpansionFileReadException e) {
	    e.printStackTrace();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return area;
    }

    @Override
    public String getMessage() {
	return PluginServices.getText(this,
		"action_add_values_to_new_construccion_was_not_performed");
    }
}
