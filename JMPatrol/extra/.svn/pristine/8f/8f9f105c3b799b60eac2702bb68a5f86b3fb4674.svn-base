package com.ecity.z3map.maploader.model;

import com.esri.core.geometry.Envelope;

import java.io.Serializable;
/**
 * Created by zhengzhuanzi on 2017/6/16.
 */

public class ECityRect implements Serializable {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private double width;
    private double height;

    public ECityRect(double xmin,double ymin,double xmax,double ymax){
        xMin = xmin;
        yMin = ymin;
        xMax = xmax;
        yMax = ymax;
        updateSize();
    }

    public ECityRect(Envelope envelope){
        xMin = envelope.getXMin();
        yMin = envelope.getYMin();
        xMax = envelope.getXMax();
        yMax = envelope.getYMax();
        updateSize();
    }

    public double getXMin() {
        return xMin;
    }

    public void setXMin(double xMin) {
        this.xMin = xMin;
        updateSize();
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(double xMax) {
        this.xMax = xMax;
        updateSize();
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(double yMin) {
        this.yMin = yMin;
        updateSize();
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(double yMax) {
        this.yMax = yMax;
        updateSize();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    private void updateSize(){
        width = Math.abs(xMax - xMin);
        height = Math.abs(yMax - yMin);
    }

    public boolean isZeroRect(){
        if(getWidth()< 0.0000001 && getHeight() < 0.0000001) {
            return true;
        }

        return false;
    }
}
