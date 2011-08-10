package es.icarto.gvsig.catastro.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.CutPolygonCADTool;

public class NewPredio {
    
    FLyrVect layer = null;
    IRowEdited selectedRow = null;

    public NewPredio(FLyrVect l, IRowEdited row) {
	layer = l;
	selectedRow = row;
    }

    public Value[] getAttributes() {
	int numAttr;
	try {
	    numAttr = layer.getRecordset().getFieldCount();
	    Value[] values = new Value[numAttr];
	    values = selectedRow.getAttributes().clone();
	    values[7] = ValueFactory.createValue("5432");
	    return values;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
