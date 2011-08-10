package es.icarto.gvsig.catastro;


import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.tools.CutPolygonCADTool;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.icarto.gvsig.catastro.actions.NewPredio;

public class ActionDispatcherExtension extends Extension implements
EndGeometryListener {

    @Override
    public void initialize() {
	CADListenerManager.removeEndGeometryListener("catastro");
	CADListenerManager.addEndGeometryListener("catastro", this);
    }

    @Override
    public void execute(String actionCommand) {
	// nothing to do
    }

    @Override
    public boolean isEnabled() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public void endGeometry(FLayer layer, String cadToolKey) {
	CADTool cadTool = CADExtension.getCADTool();
	if(cadToolKey.equalsIgnoreCase("_cut_polygon_the_new_geom") && (cadTool instanceof CutPolygonCADTool) && (layer instanceof FLyrVect)){	    
	    IRowEdited selectedRow = ((CutPolygonCADTool) cadTool).getSelectedRow();
	    NewPredio newPredio = new NewPredio((FLyrVect) layer, selectedRow);
	    Value[] values = newPredio.getAttributes();
	    ((CutPolygonCADTool) cadTool).setParametrizableValues(values);
	}
    }

}
