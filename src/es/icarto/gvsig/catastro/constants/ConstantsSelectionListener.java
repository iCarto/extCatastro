package es.icarto.gvsig.catastro.constants;

import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;

import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class ConstantsSelectionListener extends PointSelectionListener {

    TOCLayerManager tocLayerManager;

    public ConstantsSelectionListener(MapControl mc) {
	super(mc);
	tocLayerManager = new TOCLayerManager();
    }

    @Override
    public void point(PointEvent event) throws BehaviorException{
	ConstantManager constantManager = new ConstantManager();
	Constants constants = new Constants();
	constants.setRegion("region");
	constants.setManzana("manzana");
	constants.setPredio("predio");
	constantManager.setConstants(constants);
	tocLayerManager.setVisibleAllLayers();
    }
}
