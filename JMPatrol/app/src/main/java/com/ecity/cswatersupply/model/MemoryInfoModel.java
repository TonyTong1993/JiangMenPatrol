package com.ecity.cswatersupply.model;

public class MemoryInfoModel {
    private String memoryName;
    private String memoryPath;
    private boolean isCheck;

    public MemoryInfoModel() {
        super();
    }

    public MemoryInfoModel(String memoryName, String memoryPath, boolean isCheck) {
        this.memoryName = memoryName;
        this.memoryPath = memoryPath;
        this.isCheck = isCheck;
    }

    public String getMemoryName() {
        return memoryName;
    }

    public void setMemoryName(String memoryName) {
        this.memoryName = memoryName;
    }

    public String getMemoryPath() {
        return memoryPath;
    }

    public void setMemoryPath(String memoryPath) {
        this.memoryPath = memoryPath;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof MemoryInfoModel) {
            MemoryInfoModel u = (MemoryInfoModel) obj;
            return this.memoryPath.equals(u.memoryPath);
        }
        return false;
    }
}