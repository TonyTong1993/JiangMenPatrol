package com.ecity.cswatersupply.project.network.response;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ProjectAndGeoModel {
    private ProjectForMapModel attributes;
    private GeometryModel geometry;

    public ProjectForMapModel getAttributes() {
        return attributes;
    }

    public void setAttributes(ProjectForMapModel attributes) {
        this.attributes = attributes;
    }

    public GeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryModel geometry) {
        this.geometry = geometry;
    }
}
