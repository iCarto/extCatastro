package es.icarto.gvsig.catastro.actions;

public class CheckPredioIsWithinOneManzana implements ITopologicalRule {

    @Override
    public boolean isObey() {
	// TODO
	// * check the predio is only in the manzana we are working on
	return true;
    }

}
