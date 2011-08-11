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

    public boolean areConstantsSetForConstruccion(){
	if(constants!=null){
	    if((constants.getPredio() != null) && (constants.getManzana() != null) && (constants.getRegion() != null)) {
		return true;
	    }
	    return false;
	}
	return false;
    }

    public boolean areConstantsSetForPredio(){
	if(constants!=null){
	    if((constants.getManzana() != null) && (constants.getRegion() != null)) {
		return true;
	    }
	    return false;
	}
	return false;
    }

    public boolean areConstantsSetForManzana(){
	if(constants!=null){
	    if(constants.getRegion() != null) {
		return true;
	    }
	    return false;
	}
	return false;
    }

}

