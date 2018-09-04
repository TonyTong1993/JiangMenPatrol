package com.ecity.cswatersupply.model.map;
import java.io.Serializable;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

public class SearchResult implements Serializable{
    private static final long serialVersionUID = -5238327492932876939L;
    public Point centerPoint;
	public Graphic  graphic;
	public String   titleName;
	public String   address;
	public SearchResult() {
		centerPoint = new Point();
	}
	
	
}
