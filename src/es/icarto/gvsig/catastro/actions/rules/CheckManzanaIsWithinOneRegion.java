package es.icarto.gvsig.catastro.actions.rules;

import org.gvsig.fmap.core.NewFConverter;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckManzanaIsWithinOneRegion implements ITopologicalRule {

	IGeometry insertedGeometry;

	public CheckManzanaIsWithinOneRegion(IGeometry insertedGeometry) {
		this.insertedGeometry = insertedGeometry;
	}

	@Override
	public boolean isObey() {
		Geometry manzanaJTSGeom = NewFConverter.toJtsGeometry(insertedGeometry);
		Geometry regionJTSGeom = getRegionGeom();
		Geometry regionJTSToleranceBuffer = regionJTSGeom.buffer(0.5);
		if (!manzanaJTSGeom.coveredBy(regionJTSToleranceBuffer)) {
			return false;
		}
		return true;
	}

	private Geometry getRegionGeom() {
		TOCLayerManager tocLayerManager = new TOCLayerManager();
		FLyrVect region = tocLayerManager
				.getLayerByName(Preferences.REGIONES_LAYER_NAME);
		IGeometry regionGeom = getGeomFromFLyrVect(region);
		Geometry regionJTSGeom = NewFConverter.toJtsGeometry(regionGeom);
		return regionJTSGeom;
	}

	private IGeometry getGeomFromFLyrVect(FLyrVect layer) {
		ConstantManager constantManager = new ConstantManager();
		try {
			String sqlQuery = "select * from '"
					+ layer.getRecordset().getName() + "'" + " where "
					+ Preferences.REGION_NAME_IN_DB + " = '"
					+ constantManager.getConstants().getRegion() + "';";
			IFeatureIterator featureIterator = layer.getSource()
					.getFeatureIterator(sqlQuery, null);
			return featureIterator.next().getGeometry();
		} catch (ReadDriverException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getMessage() {
		return PluginServices
				.getText(this, "rule_manzana_is_not_within_region");
	}

}
