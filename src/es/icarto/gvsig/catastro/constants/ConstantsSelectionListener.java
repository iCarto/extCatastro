package es.icarto.gvsig.catastro.constants;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class ConstantsSelectionListener extends PointSelectionListener {

    private static final int LAYER_IS_MANZANA = 0;
    private static final int LAYER_IS_PREDIO = 1;
    private static final int LAYER_IS_REGION = 2;

    TOCLayerManager tocLayerManager;
    private ConstantManager constantManager;
    private Constants constants;
    private HashMap<String, Integer> layerCodes;

    public ConstantsSelectionListener(MapControl mc) {
	super(mc);
	tocLayerManager = new TOCLayerManager();
	constantManager = new ConstantManager();
	constants = new Constants();
	layerCodes = new HashMap<String, Integer>();
	initLayerCodes();
    }

    private void initLayerCodes(){
	layerCodes.put(Preferences.MANZANAS_LAYER_NAME, LAYER_IS_MANZANA);
	layerCodes.put(Preferences.PREDIOS_LAYER_NAME, LAYER_IS_PREDIO);
	layerCodes.put(Preferences.REGIONES_LAYER_NAME, LAYER_IS_REGION);
    }

    @Override
    public void point(PointEvent event) throws BehaviorException{
	super.point(event);
	FLyrVect layer = tocLayerManager.getActiveLayer();
	String layerName = layer.getName();
	try {
	    FBitSet selectionIndex = layer.getRecordset().getSelection();
	    int indexInRecordset = selectionIndex.nextSetBit(0);
	    if(indexInRecordset != -1){
		Value[] values = layer.getRecordset().getRow(indexInRecordset);
		String[] fieldNames = layer.getRecordset().getFieldNames();
		int predioIndex = -1;
		int manzanaIndex = -1;
		int regionIndex = -1;
		for (int i=0; i<fieldNames.length; i++){
		    if(fieldNames[i].equalsIgnoreCase(Preferences.MANZANA_NAME_IN_DB)){
			manzanaIndex = i;
		    } else if (fieldNames[i].equalsIgnoreCase(Preferences.REGION_NAME_IN_DB)){
			regionIndex = i;
		    } else if (fieldNames[i].equalsIgnoreCase(Preferences.PREDIO_NAME_IN_DB)){
			predioIndex = i;
		    }
		}
		switch(layerCodes.get(layerName)){
		case LAYER_IS_PREDIO:
		    constants.setManzana(values[manzanaIndex].toString());
		    constants.setPredio(values[predioIndex].toString());
		    constants.setRegion(values[regionIndex].toString());
		    break;
		case LAYER_IS_MANZANA:
		    constants.setManzana(values[manzanaIndex].toString());
		    constants.setRegion(values[regionIndex].toString());
		    break;
		case LAYER_IS_REGION:
		    constants.setRegion(values[regionIndex].toString());
		    break;
		}
		if(constants != null){
		    int option = JOptionPane.showConfirmDialog(null, PluginServices.getText(this, "selection_confirm"), "Confirm selection",
			    JOptionPane.OK_CANCEL_OPTION, JOptionPane.OK_CANCEL_OPTION,
			    null);
		    if(option == JOptionPane.OK_OPTION){
			constantManager.setConstants(constants);
			tocLayerManager.setVisibleAllLayers();
		    } else {
			constants.clear();
		    }
		} else {
		    Object[] options = { "OK" };
		    JOptionPane.showOptionDialog(null, PluginServices.getText(this, "selection_is_none"), "Warning",
			    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
			    null, options, options[0]);
		}
	    } else {
		// launch dialog asking for a new selection
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }
}
