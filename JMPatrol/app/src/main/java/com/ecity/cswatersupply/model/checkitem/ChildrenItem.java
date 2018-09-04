
package com.ecity.cswatersupply.model.checkitem;

public class ChildrenItem {
    
	private String name;
	private String alias;
	private boolean isVisible;

    public ChildrenItem() {
	}
	
	public ChildrenItem(String name,String alias,boolean visible) {
		this.name = name;
		this.alias = alias;
		this.isVisible = visible;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	   
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
