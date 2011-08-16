package es.icarto.gvsig.catastro.constants;

public class ConstantManager {

    //class variable for all clients to know the constants
    private static Constants constants = null;

    public ConstantManager(){
    }

    public void setConstants(Constants constants){
	ConstantManager.constants = constants;
    }

    public Constants getConstants(){
	if(constants == null){
	    constants = new Constants();
	    return constants;
	}
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
	/*
	 * The predio needs also to be selected,
	 * as we will cut the construcciones, we need to know the predio ID.
	 */
	if(constants!=null){
	    if((constants.getManzana() != null) && (constants.getRegion() != null) && (constants.getPredio() != null)) {
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

