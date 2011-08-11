package es.icarto.gvsig.catastro.constants;

public class Constants {

    private String region;
    private String manzana;
    private String predio;

    public Constants(){
	region = null;
	manzana = null;
	predio = null;
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
}
