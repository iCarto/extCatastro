package es.icarto.gvsig.catastro;


import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;

import es.icarto.gvsig.catastro.actions.UpdatePredioID;

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
	UpdatePredioID dividePredio = new UpdatePredioID();
	dividePredio.execute();
    }

}
