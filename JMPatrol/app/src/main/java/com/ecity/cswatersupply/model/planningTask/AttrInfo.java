package com.ecity.cswatersupply.model.planningTask;

import java.io.Serializable;

public class AttrInfo implements Serializable {
    private String title;
    private String content;

    public AttrInfo(String t, String c) {
        this.content = c;
        this.title = t;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
