package es.icarto.gvsig.catastro.actions.rules;

public class UpdateConstructionsGeom implements ITopologicalRule {

    @Override
    public boolean isObey() {
	// TODO
	// * if new division cuts some constructions, cut them all
	return true;
    }

    @Override
    public String getMessage() {
	// TODO Auto-generated method stub
	return null;
    }

}
