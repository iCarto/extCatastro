package es.icarto.gvsig.catastro.actions;

import org.gvsig.fmap.core.NewFConverter;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckManzanaIsOverlapingAnotherOne implements ITopologicalRule {

	IGeometry insertedGeometry;

	public CheckManzanaIsOverlapingAnotherOne(IGeometry insertedGeometry) {
		this.insertedGeometry = insertedGeometry;
	}

	@Override
	public boolean isObey() {
		boolean checkRule = false;
		Geometry manzanaJTSGeom = NewFConverter.toJtsGeometry(insertedGeometry);
		TOCLayerManager tocLayerManager = new TOCLayerManager();
		FLyrVect manzanasLayer = tocLayerManager.getLayerManzana();
		SelectableDataSource manzanasLayerRecordset;
		try {
			manzanasLayerRecordset = manzanasLayer.getRecordset();
			ReadableVectorial manzanasLayerSourceFeats = manzanasLayer
					.getSource();
			for (int i = 0; i < manzanasLayerRecordset.getRowCount(); i++) {
				IGeometry gvGeom = manzanasLayerSourceFeats.getShape(i);
				if (!gvGeom.equals(manzanaJTSGeom)) {
					Geometry auxJTSGeom = NewFConverter.toJtsGeometry(gvGeom);
					if (!manzanaJTSGeom.overlaps(auxJTSGeom)) {
						checkRule = true;
					} else {
						checkRule = false;
						break;
					}
				}
			}
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkRule;
	}

	@Override
	public String getMessage() {
		return PluginServices.getText(this, "overlaps_manzana_message");
	}
}