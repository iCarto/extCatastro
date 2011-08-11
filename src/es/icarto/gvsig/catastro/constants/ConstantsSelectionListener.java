package es.icarto.gvsig.catastro.constants;

import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiFrame.NewStatusBar;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;

import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class ConstantsSelectionListener extends PointSelectionListener {

	private static final int LAYER_IS_MANZANA = 0;
	private static final int LAYER_IS_PREDIO = 1;
	private static final int LAYER_IS_REGION = 2;

	TOCLayerManager tocLayerManager;
	private final ConstantManager constantManager;
	private final Constants constants;
	private final HashMap<String, Integer> layerCodes;

	MDIFrame mF;
	NewStatusBar footerStatusBar;

	public ConstantsSelectionListener(MapControl mc) {
		super(mc);
		tocLayerManager = new TOCLayerManager();
		constantManager = new ConstantManager();
		constants = new Constants();
		layerCodes = new HashMap<String, Integer>();
		initLayerCodes();
		initConstantsStatusBar();
	}

	private void initLayerCodes() {
		layerCodes.put(Preferences.MANZANAS_LAYER_NAME, LAYER_IS_MANZANA);
		layerCodes.put(Preferences.PREDIOS_LAYER_NAME, LAYER_IS_PREDIO);
		layerCodes.put(Preferences.REGIONES_LAYER_NAME, LAYER_IS_REGION);
	}

	@Override
	public void point(PointEvent event) throws BehaviorException {
		super.point(event);
		FLyrVect layer = tocLayerManager.getActiveLayer();
		String layerName = layer.getName();

		try {
			FBitSet selectionIndex;
			selectionIndex = layer.getRecordset().getSelection();
			int indexOfFeatureSelected = selectionIndex.nextSetBit(0);
			if (indexOfFeatureSelected != -1) {
				Value[] values = layer.getRecordset().getRow(
						indexOfFeatureSelected);
				String[] fieldNames = layer.getRecordset().getFieldNames();
				int predioIndex = -1;
				int manzanaIndex = -1;
				int regionIndex = -1;
				for (int i = 0; i < fieldNames.length; i++) {
					if (fieldNames[i]
							.equalsIgnoreCase(Preferences.MANZANA_NAME_IN_DB)) {
						manzanaIndex = i;
					} else if (fieldNames[i]
							.equalsIgnoreCase(Preferences.REGION_NAME_IN_DB)) {
						regionIndex = i;
					} else if (fieldNames[i]
							.equalsIgnoreCase(Preferences.PREDIO_NAME_IN_DB)) {
						predioIndex = i;
					}
				}
				switch (layerCodes.get(layerName)) {
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
				if (constants != null) {
					int option = JOptionPane.showConfirmDialog(null,
							PluginServices.getText(this, "selection_confirm"),
							"Confirm selection", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.OK_CANCEL_OPTION, null);
					if (option == JOptionPane.OK_OPTION) {
						constantManager.setConstants(constants);
						updateConstantsStatusBar();
						tocLayerManager.setVisibleAllLayers();
						IGeometry geom = layer.getSource().getFeature(
								indexOfFeatureSelected).getGeometry();
						layer.getMapContext().getViewPort().setExtent(
								geom.getBounds2D());
					} else {
						constants.clear();
						clearConstantsStatusBar();
						layer.getRecordset().clearSelection();
					}
				} else {
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(null, PluginServices.getText(
							this, "selection_is_none"), "Warning",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[0]);
				}
			}
		} catch (ReadDriverException e) {
			e.printStackTrace();
		}
	}

	private void initConstantsStatusBar() {
		mF = (MDIFrame) PluginServices.getMainFrame();
		footerStatusBar = mF.getStatusBar();
	}

	private void updateConstantsStatusBar() {
		Constants constants = constantManager.getConstants();
		String constantsInfo = "R: " + constants.getRegion() + " M: "
				+ constants.getManzana() + " P: " + constants.getPredio();
		JLabel constantsLabel = new JLabel(constantsInfo);
		footerStatusBar.add(constantsLabel);
	}

	private void clearConstantsStatusBar() {
		JLabel clearConstantsLabel = new JLabel("R:- M:- P:-");
		footerStatusBar.add(clearConstantsLabel);
	}
}
