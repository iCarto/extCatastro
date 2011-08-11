package es.icarto.gvsig.catastro.utils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class TOCLayerManager {


    private FLayers layersInTOC;
    private MapControl mapControl;

    public TOCLayerManager(){
	BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
	mapControl = view.getMapControl();
	layersInTOC = mapControl.getMapContext().getLayers();
    }

    public void setActiveAndVisibleLayersForManzanas() {
	layersInTOC.setAllActives(false);
	layersInTOC.setAllVisibles(false);
	for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
	    String layerName = layersInTOC.getLayer(i).getName();
	    if (layerName.equalsIgnoreCase(Preferences.PREDIOS_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
	    } else if (layerName
		    .equalsIgnoreCase(Preferences.MANZANAS_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    public void setActiveAndVisibleLayersForPredios() {
	layersInTOC.setAllActives(false);
	layersInTOC.setAllVisibles(false);
	for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
	    String layerName = layersInTOC.getLayer(i).getName();
	    if (layerName
		    .equalsIgnoreCase(Preferences.CONSTRUCCIONES_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
	    } else if(layerName.equalsIgnoreCase(Preferences.MANZANAS_LAYER_NAME)){
		layersInTOC.getLayer(i).setVisible(true);
	    }
	    else if (layerName
		    .equalsIgnoreCase(Preferences.PREDIOS_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    public void setActiveAndVisibleLayersForConstrucciones() {
	layersInTOC.setAllActives(false);
	layersInTOC.setAllVisibles(false);
	for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
	    String layerName = layersInTOC.getLayer(i).getName();
	    if (layerName.equalsIgnoreCase(Preferences.PREDIOS_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
	    } else if (layerName
		    .equalsIgnoreCase(Preferences.CONSTRUCCIONES_LAYER_NAME)) {
		layersInTOC.getLayer(i).setVisible(true);
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    public void setVisibleAllLayers() {
	layersInTOC.setAllVisibles(true);
    }


    public void setActiveAndVisibleLayer(String layerName){
	layersInTOC.setAllVisibles(false);
	layersInTOC.setAllActives(false);
	for (int i=0; i<layersInTOC.getLayersCount(); i++){
	    FLayer layer = layersInTOC.getLayer(i);
	    String name = layer.getName();
	    if(name.equalsIgnoreCase(layerName)){
		layer.setVisible(true);
		layer.setActive(true);
	    }
	}
    }

    public FLyrVect getLayerByName(String layerName){
	for (int i=0; i<layersInTOC.getLayersCount(); i++){
	    if(layersInTOC.getLayer(i).getName().equalsIgnoreCase(layerName)){
		return (FLyrVect) layersInTOC.getLayer(i);
	    }
	}
	return null;
    }

    public FLyrVect getActiveLayer(){
	return (FLyrVect) mapControl.getMapContext().getLayers().getActives()[0];
    }

    public String getNameOfActiveLayer(){
	return mapControl.getMapContext().getLayers().getActives()[0].getName();
    }
}
