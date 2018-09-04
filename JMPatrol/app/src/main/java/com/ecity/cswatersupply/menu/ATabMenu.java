package com.ecity.cswatersupply.menu;

import java.io.Serializable;

public class ATabMenu implements Serializable {
    private static final long serialVersionUID = 3159321405698797155L;
    private String name;

    public ATabMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
