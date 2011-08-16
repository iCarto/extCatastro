package es.icarto.gvsig.catastro.constants;

import es.icarto.gvsig.catastro.utils.Preferences;

public class Constants {

    private String region = null;
    private String manzana = null;
    private String predio = null;

    public Constants(){
	clear();
    }

    public void setRegion(String region){
	this.region = region;
    }

    public void setManzana(String manzana){
	this.manzana = manzana;
    }

    public void setPredio(String predio){
	this.predio = predio;
    }

    public String getRegion(){
	return this.region;
    }

    public String getManzana(){
	return this.manzana;
    }

    public String getPredio(){
	return this.predio;
    }

    public void clear() {
	region = null;
	manzana = null;
	predio = null;
    }

    public String getPais() {
	//TODO: este campo se debe recuperar de una tabla de la BD
	return Preferences.PAIS;
    }

    public String getEstado() {
	//TODO: este campo se debe recuperar de una tabla de la BD
	return Preferences.ESTADO;
    }

    public String getMunicipio() {
	//TODO: este campo se debe recuperar de una tabla de la BD
	return Preferences.MUNICIPIO;
    }

    public String getLimiteMunicipal() {
	//TODO: este campo se debe recuperar de una tabla de la BD
	return Preferences.LIMITE;
    }

}
