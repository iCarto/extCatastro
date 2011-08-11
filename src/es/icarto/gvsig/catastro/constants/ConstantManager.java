package es.icarto.gvsig.catastro.constants;

public class ConstantManager {

    //class variable for all clients to know the constants
    private static Constants constants;

    public ConstantManager(){
	constants = null;
    }

    public void setConstants(Constants constants){
	this.constants = constants;
    }

    public Constants getConstants(){
	return constants;
    }

    public boolean areConstantsSet(){
	if(constants!=null){
	    return true;
	}
	return false;
    }
}
