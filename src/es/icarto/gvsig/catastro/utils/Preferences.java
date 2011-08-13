package es.icarto.gvsig.catastro.utils;

public class Preferences {

    public static final String MANZANAS_LAYER_NAME = "manzanas";
    public static final String PREDIOS_LAYER_NAME = "predios";
    public static final String CONSTRUCCIONES_LAYER_NAME = "construcciones";
    public static final String REGIONES_LAYER_NAME = "regiones";

    public static final String ESTADO_NAME_IN_DB = "EDO_CVE";
    public static final String MUNICIPIO_NAME_IN_DB = "MUN_CVE";
    public static final String LIMITE_NAME_IN_DB = "LIM_CVE";
    public static final String PREDIO_NAME_IN_DB = "PRE_CVE";
    public static final String REGION_NAME_IN_DB = "REG_CVE";
    public static final String MANZANA_NAME_IN_DB = "MAN_CVE";

    /*
     * Those constants should be set when the program starts and they must be
     * taken from the database taking into account database connection
     * information. For now they are been "hardcode" here.
     */
    public static final String ESTADO = "29";
    public static final String MUNICIPIO = "010";
    public static final String LIMITE = "0001";
}
