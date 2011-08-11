package es.icarto.gvsig.catastro.utils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class TOCLayerManager {

    private FLayers layersInTOC;

    public TOCLayerManager(){
	BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
	MapControl mapControl = view.getMapControl();
	layersInTOC = mapControl.getMapContext().getLayers();
    }

    public void setActiveAndVisibleLayersForPredios(){
	layersInTOC.setAllActives(false);
	layersInTOC.setAllVisibles(false);
	for (int i=0; i<layersInTOC.getLayersCount(); i++){
	    String layerName = layersInTOC.getLayer(i).getName();
	    if(layerName.equalsIgnoreCase("construcciones")){
		layersInTOC.getLayer(i).setVisible(true);
	    } else if(layerName.equalsIgnoreCase("predios")){
		layersInTOC.getLayer(i).setVisible(true);
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    public void setActiveAndVisibleLayersForConstrucciones(){
	layersInTOC.setAllActives(false);
	layersInTOC.setAllVisibles(false);
	for (int i=0; i<layersInTOC.getLayersCount(); i++){
	    String layerName = layersInTOC.getLayer(i).getName();
	    if(layerName.equalsIgnoreCase("predios")){
		layersInTOC.getLayer(i).setVisible(true);
	    } else if(layerName.equalsIgnoreCase("construcciones")){
		layersInTOC.getLayer(i).setVisible(true);
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    public void setVisibleAllLayers(){
	layersInTOC.setAllVisibles(true);
    }

    public FLyrVect getLayerManzana(){
	for (int i=0; i<layersInTOC.getLayersCount(); i++){
	    if(layersInTOC.getLayer(i).getName().equalsIgnoreCase(Preferences.MANZANA_LAYER_NAME)){
		return (FLyrVect) layersInTOC.getLayer(i);
	    }
	}
	return null;
    }
}
