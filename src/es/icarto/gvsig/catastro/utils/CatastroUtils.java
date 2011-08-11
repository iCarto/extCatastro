package es.icarto.gvsig.catastro.utils;

import java.awt.geom.Rectangle2D;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionManager;
import com.iver.cit.gvsig.Version;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ISpatialDB;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.SelectionSupport;
import com.iver.cit.gvsig.fmap.layers.SpatialCache;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

public class CatastroUtils {

    private static WKTWriter geometryWriter = new WKTWriter();
    private static WKTReader geometryReader = new WKTReader();
    private static XMLEntity xml = null;
    private static MapControl mapControl;

    /*
     * Methods taken by CopyFeatures Extension by gvSIG
     */

    public static void copyFeatures(FLyrVect sourceLayer)
	    throws ReadDriverException {
	if (sourceLayer != null) {
	    XMLEntity xmlRoot = newRootNode();
	    xmlRoot.putProperty("shapeType", sourceLayer.getShapeType());
	    SelectableDataSource rs;

	    try {
		rs = sourceLayer.getRecordset();
		ReadableVectorial rv = sourceLayer.getSource();
		rv.start();
		SelectionSupport selectionSupport = rs.getSelectionSupport();

		FieldDescription[] fieldsDescription = rs
			.getFieldsDescription();
		String[] fields = new String[fieldsDescription.length];
		XMLEntity xmlFields = new XMLEntity();
		xmlFields.setName("fields");
		for (int i = 0; i < fieldsDescription.length; i++) {
		    xmlFields.addChild(getFieldXML(fieldsDescription[i]));
		    fields[i] = fieldsDescription[i].getFieldName();
		}
		xmlRoot.addChild(xmlFields);

		XMLEntity xmlFeatures = new XMLEntity();
		xmlFeatures.setName("features");

		IFeature feat = null;
		XMLEntity featureXML;
		for (int numReg = 0; numReg < rv.getShapeCount(); numReg++) {
		    feat = rv.getFeature(numReg);
		    int selectionIndex = -1;
		    if (rv instanceof ISpatialDB) {
			selectionIndex = ((ISpatialDB) rv)
				.getRowIndexByFID(feat);
		    } else {
			selectionIndex = Integer.parseInt(feat.getID());
		    }
		    if (selectionIndex != -1) {
			if (!selectionSupport.isSelected(selectionIndex)) {
			    continue;
			}
		    }

		    featureXML = getFeatureXML(feat, fieldsDescription);
		    xmlFeatures.addChild(featureXML);

		}
		xmlRoot.addChild(xmlFeatures);
		PluginServices.putInClipboard(xmlRoot.toString());
		rv.stop();
	    } finally {
	    }
	}
    }

    private static XMLEntity newRootNode() {
	XMLEntity xml = new XMLEntity();
	fillXMLRootNode(xml);
	return xml;
    }

    private static void fillXMLRootNode(XMLEntity xml) {
	xml.putProperty("applicationName", "gvSIG");
	xml.putProperty("version", Version.format());
	xml.putProperty("type", "GeometryCopy");
    }

    private static XMLEntity getFeatureXML(IFeature feat,
	    FieldDescription[] fieldsDescription) throws ReadDriverException {
	XMLEntity xml = new XMLEntity();
	xml.setName("feature");
	for (int i = 0; i < fieldsDescription.length; i++) {
	    xml.putProperty(fieldsDescription[i].getFieldName(), feat
		    .getAttribute(i).toString());
	}
	xml.addChild(getGeometryXML(feat.getGeometry()));
	return xml;
    }

    private static XMLEntity getFieldXML(FieldDescription fd) {
	XMLEntity xml = new XMLEntity();

	xml.setName("field");
	xml.putProperty("fieldAlias", fd.getFieldAlias());
	xml.putProperty("fieldName", fd.getFieldName());
	xml.putProperty("fieldDecimalCount", fd.getFieldDecimalCount());
	xml.putProperty("fieldType", fd.getFieldType());

	return xml;
    }

    private static XMLEntity getGeometryXML(IGeometry geom) {
	XMLEntity xml = new XMLEntity();

	xml.setName("geometry");
	xml.putProperty("geometry", geometryWriter.write(geom.toJTSGeometry()));

	return xml;
    }

    public static void pasteFeatures(FLyrVect destinationLayer)
	    throws Exception {
	xml = getCheckedXMLFromClipboard();
	if (xml != null) {
	    CADExtension.initFocus();
	    CADExtension.getEditionManager().setMapControl(mapControl);

	    View vista = (View) PluginServices.getMDIManager()
		    .getActiveWindow();
	    mapControl = vista.getMapControl();
	    EditionManager em = CADExtension.getEditionManager();
	    if (em.getActiveLayerEdited() != null) {
		VectorialLayerEdited vle = (VectorialLayerEdited) em
			.getActiveLayerEdited();
		VectorialEditableAdapter vea = vle.getVEA();
		destinationLayer = (FLyrVect) vle.getLayer();
		MapContext mapContext = destinationLayer.getMapContext();

		int shapeType = xml.getIntProperty("shapeType");
		if (shapeType != destinationLayer.getShapeType()) {
		    return;
		}
		int child = xml.firstIndexOfChild("name", "fields");
		if (child == -1) {
		    return;
		}
		FieldDescription[] fieldsDescription = getFieldsDescription(xml
			.getChild(child));

		child = xml.firstIndexOfChild("name", "features");
		if (child == -1) {
		    return;
		}

		XMLEntity featuresXML = xml.getChild(child);
		if (featuresXML == null) {
		    return;
		}

		SelectableDataSource myRs = destinationLayer.getRecordset();
		mapContext.beginAtomicEvent();
		myRs.start();
		try {
		    int featuresCount = featuresXML.getChildrenCount();
		    for (int i = 0; i < featuresCount; i++) { // bucle por las
			// features
			XMLEntity featureXML = featuresXML.getChild(i);
			Value[] values = new Value[myRs.getFieldCount()];
			for (int j = 0; j < values.length; j++) { // bucle por
			    // los
			    // campos de
			    // myRs para
			    // rellenarlos
			    String name = myRs.getFieldName(j);
			    int type = myRs.getFieldType(j);
			    values[j] = ValueFactory.createNullValue();
			    for (int k = 0; k < fieldsDescription.length; k++) { // bucle
				// buscando
				// el
				// campo
				// que
				// queremos
				// rellenar
				if (fieldsDescription[k].getFieldName()
					.compareTo(name) == 0
					&& fieldsDescription[k].getFieldType() == type) {
				    String stringValue = featureXML
					    .getStringProperty(name);
				    values[j] = ValueFactory.createValueByType(
					    stringValue, type);
				}
			    }
			}

			child = featureXML
				.firstIndexOfChild("name", "geometry");
			if (child == -1) {
			    continue;
			}
			XMLEntity geometryXML = featureXML.getChild(child);
			if (geometryXML == null) {
			    return;
			}
			IGeometry geom = FConverter
				.jts_to_igeometry(geometryReader
					.read(geometryXML
						.getStringProperty("geometry"))); // .cloneGeometry();
			if (geom != null) {
			    String newFID = vea.getNewFID();
			    DefaultFeature df = new DefaultFeature(geom,
				    values, newFID);
			    vea.addRow(df, getName(), EditionEvent.GRAPHIC);

			    SpatialCache spatialCache = destinationLayer
				    .getSpatialCache();
			    Rectangle2D r = geom.getBounds2D();
			    if (geom.getGeometryType() == FShape.POINT) {
				r = new Rectangle2D.Double(r.getX(), r.getY(),
					1, 1);
			    }
			    spatialCache.insert(r, geom);

			    CADExtension.getCADToolAdapter().getMapControl()
			    .rePaintDirtyLayers();

			}
		    }
		} catch (Exception e) {
		    throw e;
		} finally {
		    myRs.stop();
		    mapContext.endAtomicEvent();
		}

	    }
	}
    }

    private static XMLEntity getCheckedXMLFromClipboard() {
	String sourceString = PluginServices.getFromClipboard();
	if (sourceString == null) {
	    return null;
	}

	XMLEntity xml = null;
	try {
	    xml = XMLEntity.parse(sourceString);
	} catch (org.exolab.castor.xml.MarshalException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (org.exolab.castor.xml.ValidationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try {
	    if (xml.getStringProperty("applicationName").compareTo("gvSIG") != 0) {
		return null;
	    }
	    // Comentarizar esto para que se admita la copia entre versiones
	    if (xml.getStringProperty("version").compareTo(Version.format()) != 0) {
		return null;
	    }
	    if (xml.getStringProperty("type").compareTo("GeometryCopy") != 0) {
		return null;
	    }
	} catch (NotExistInXMLEntity e) {
	    return null;
	}
	return xml;
    }

    private static FieldDescription[] getFieldsDescription(XMLEntity xml) {
	if (xml.getName().compareTo("fields") != 0) {
	    throw new NotExistInXMLEntity();
	}
	int childrenCount = xml.getChildrenCount();
	FieldDescription[] fieldsDescription = new FieldDescription[childrenCount];
	for (int i = 0; i < childrenCount; i++) {
	    XMLEntity fieldChild = xml.getChild(i);
	    fieldsDescription[i] = getFieldDescription(fieldChild);
	}
	return fieldsDescription;
    }

    private static FieldDescription getFieldDescription(XMLEntity xml) {
	if (xml.getName().compareTo("field") != 0) {
	    throw new NotExistInXMLEntity();
	}
	FieldDescription fieldDescription = new FieldDescription();
	fieldDescription.setFieldAlias(xml.getStringProperty("fieldAlias"));
	fieldDescription.setFieldName(xml.getStringProperty("fieldName"));
	fieldDescription.setFieldDecimalCount(xml
		.getIntProperty("fieldDecimalCount"));
	fieldDescription.setFieldType(xml.getIntProperty("fieldType"));

	return fieldDescription;
    }

    public static String getName() {
	return "paste_feature_";
    }
}
