/*
 * Created on 18-sep-2007
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: JtsUtil.java 25601 2008-11-27 22:14:32Z azabala $
 * $Log: JtsUtil.java,v $
 * Revision 1.1  2007/09/19 09:01:20  azabala
 * first version in cvs
 *
 *
 */
package es.icarto.gvsig.catastro.utils;

import java.util.ArrayList;
import java.util.List;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.util.PolygonExtracter;

/**
 * Utility methods for JTS library use. Taken from libTopology, developed by azabala
 * 
 */
public class JtsUtil {

    public static Polygon[] extractPolygons(Geometry g) {
	Polygon[] solution = null;
	List<Polygon> solutionList = new ArrayList<Polygon>();
	if (g instanceof Polygon) {
	    solutionList.add((Polygon) g);
	} else if (g instanceof GeometryCollection) {
	    GeometryCollection geomCol = (GeometryCollection) g;
	    List polygons = PolygonExtracter.getPolygons(geomCol);
	    solutionList.addAll(polygons);
	}
	solution = new Polygon[solutionList.size()];
	solutionList.toArray(solution);
	return solution;
    }

}
