package com.ecity.mobile.android.crossanalysis;

import java.util.Hashtable;
import java.util.Map;

/**
 * the data structure of pipe attribute.
 * @author GK
 *
 */
public class Lininfo{
	private String gid,color,centerX,centerY,rad;
	private Hashtable<String, String> linAtt,netAtt;
	public Lininfo(){
		this.linAtt = new Hashtable<String, String> (200,(float)0.8);
		this.netAtt = new Hashtable<String, String> (100,(float)0.8);
	}
	
	public String getGid(){
		return gid;	
	}
	public String getColor(){
		return color;	
	}
	public String getCenterX(){
		return centerX;	
	}
	public String getCenterY(){
		return centerY;	
	}
	public String getRad(){
		return rad;	
	}
	public Hashtable<String, String> getLinAtt(){
		return linAtt;	
	}
	public Hashtable<String, String> getNetAtt(){
		return netAtt;	
	}

	public void setGid(String gid){
		this.gid=gid;		
	}
	public void setColor(String color){
		this.color=color;		
	}
	public void setCenterX(String centerX){
		this.centerX=centerX;		
	}
	public void setCenterY(String centerY){
		this.centerY=centerY;		
	}
	public void setRad(String rad){
		this.rad=rad;		
	}
	public void setLinAtt(String str1,String str2){	
		this.linAtt.put(str1, str2);
	}
	public void setNetAtt(String str1,String str2){
		this.netAtt.put(str1, str2);
	}
	public void putLinAttribute(Map<String, String> attribute) {
		if(null == attribute)
			return;
		for (Map.Entry<String, String> entry : attribute.entrySet()) {
			if(null == entry.getValue())
			{
				setLinAtt(entry.getKey(),"[空值]");
			}
			else
				setLinAtt(entry.getKey(),entry.getValue());
		}
	}
}
