 
package com.ecity.cswatersupply.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.esri.core.map.Graphic;

public class QueryResultShowModel implements Serializable {
	private static final long serialVersionUID = 1L;
	public ArrayList<Graphic> graphics = null;
	public int currentItem = 0;
}
