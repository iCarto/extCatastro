package es.icarto.gvsig.catastro.actions;

import org.gvsig.fmap.core.NewFConverter;

import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckPredioIsWithinOneManzana implements ITopologicalRule {

    IRowEdited selectedRow;
    public CheckPredioIsWithinOneManzana(IRowEdited selectedRow){
	this.selectedRow = selectedRow;
    }

    @Override
    public boolean isObey() {
	// TODO
	// * check the predio is within the manzana we are working on
	IGeometry predioGeom = ((IFeature) selectedRow.getLinkedRow()).getGeometry();
	Geometry predioJTSGeom = NewFConverter.toJtsGeometry(predioGeom);
	Geometry manzanaJTSGeom = getManzanaGeom();
	predioJTSGeom.within(manzanaJTSGeom);
	return true;
    }

    private Geometry getManzanaGeom() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	FLyrVect manzana = tocLayerManager.getLayerManzana();
	IGeometry manzanaGeom = getGeomFromFLyrVect(manzana);
	Geometry manzanaJTSGeom = NewFConverter.toJtsGeometry(manzanaGeom);
	return manzanaJTSGeom;
    }

    private IGeometry getGeomFromFLyrVect(FLyrVect layer) {
	return null;
	//TODO: coller dende capa
    }

}
