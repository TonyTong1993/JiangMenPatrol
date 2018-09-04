package com.ecity.cswatersupply.model.planningTask;

import com.esri.core.geometry.Geometry;

public class ArriveInfo {
    private int planTaskId;
    private Z3PlanTaskPart mZ3PlanTaskPart;
    private Geometry mGeometry;

    public int getPlanTaskId() {
        return planTaskId;
    }

    public void setPlanTaskId(int planTaskId) {
        this.planTaskId = planTaskId;
    }
    
    public Z3PlanTaskPart getmZ3PlanTaskPart() {
        return mZ3PlanTaskPart;
    }

    public void setmZ3PlanTaskPart(Z3PlanTaskPart mZ3PlanTaskPart) {
        this.mZ3PlanTaskPart = mZ3PlanTaskPart;
    }

    public Geometry getmGeometry() {
        return mGeometry;
    }

    public void setmGeometry(Geometry mGeometry) {
        this.mGeometry = mGeometry;
    }
}
