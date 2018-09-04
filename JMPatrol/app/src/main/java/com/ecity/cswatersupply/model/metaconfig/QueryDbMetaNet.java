package com.ecity.cswatersupply.model.metaconfig;

import com.ecity.cswatersupply.model.metaconfig.DbMetaNet;

public class QueryDbMetaNet extends DbMetaNet {
    
    private static final long serialVersionUID = 1L;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
