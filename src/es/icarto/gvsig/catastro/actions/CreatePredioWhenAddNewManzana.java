package es.icarto.gvsig.catastro.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.utils.CatastroUtils;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class CreatePredioWhenAddNewManzana {

	private static FLyrVect sourceLayer;

	public CreatePredioWhenAddNewManzana(FLyrVect layer) {
		this.sourceLayer = layer;
		try {
			CatastroUtils.copyFeatures(sourceLayer);
			ToggleEditing te = new ToggleEditing();
			te.stopEditing(layer, false);
			getDestinationLayer().setActive(true);
			te.startEditing(getDestinationLayer());
			CatastroUtils.pasteFeatures((FLyrVect) getDestinationLayer());
			te.stopEditing(getDestinationLayer(), false);
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FLayer getDestinationLayer() {
		return CatastroUtils.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
	}
}
