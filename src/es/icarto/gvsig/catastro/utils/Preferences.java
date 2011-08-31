package es.icarto.gvsig.catastro.utils;

public class Preferences {

    public static final String MANZANAS_LAYER_NAME = "manzanas";
    public static final String PREDIOS_LAYER_NAME = "predios";
    public static final String CONSTRUCCIONES_LAYER_NAME = "construcciones";
    public static final String REGIONES_LAYER_NAME = "regiones";

    public static final String PAIS_NAME_IN_DB = "pais_cve";
    public static final String ESTADO_NAME_IN_DB = "edo_cve";
    public static final String MUNICIPIO_NAME_IN_DB = "mun_cve";
    public static final String LIMITE_NAME_IN_DB = "lim_cve";
    public static final String PREDIO_NAME_IN_DB = "pre_cve";
    public static final String PREDIO_AREA_NAME_IN_DB = "pre_area";
    public static final String REGION_NAME_IN_DB = "reg_cve";
    public static final String MANZANA_NAME_IN_DB = "man_cve";
    public static final String MANZANA_AREA_NAME_IN_DB = "man_area";
    public static final String CONSTRUCCIONES_NAME_IN_DB = "cons_id";
    public static final String CONSTRUCCIONES_AREA_NAME_IN_DB = "cons_area";
    public static final String CATMETADATO_IN_DB = "catmetadato_id";
    public static final String GID_IN_DB = "gid";
    public static final String ZONA_NAME_IN_DB = "zon_id";

    /*
     * Those constants should be set when the program starts and they must be
     * taken from the database taking into account database connection
     * information. For now they are been "hardcode" here.
     */
    public static final String PAIS = "52";
    public static final String ESTADO = "25";
    public static final String MUNICIPIO = "6";
    public static final String LIMITE = "1";
}
