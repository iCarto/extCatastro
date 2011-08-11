package es.icarto.gvsig.catastro.actions;

import org.gvsig.fmap.core.NewFConverter;

import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.vividsolutions.jts.geom.Geometry;

public class CheckManzanaIsWithinOneRegion implements ITopologicalRule {

	IRowEdited selectedRow;

	public CheckManzanaIsWithinOneRegion(IRowEdited selectedRow) {
		this.selectedRow = selectedRow;
	}

	@Override
	public boolean isObey() {
		IGeometry manzanaGeom = ((IFeature) selectedRow.getLinkedRow())
				.getGeometry();
		Geometry manzanaJTSGeom = NewFConverter.toJtsGeometry(manzanaGeom);
		Geometry regionJTSGeom = getRegionGeom();
		manzanaJTSGeom.within(regionJTSGeom);
		return true;
	}

	private Geometry getRegionGeom() {
		return null;

	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
