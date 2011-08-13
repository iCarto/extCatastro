package es.icarto.gvsig.catastro.actions;

public interface IAction {

    public boolean execute();

    public String getName();

    public String getMessage();
}
