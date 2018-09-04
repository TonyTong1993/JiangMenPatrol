package com.ecity.cswatersupply.project.network.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/27.
 */

public class SafeDetailStepModel implements Serializable {
    private String step;
    private String stepname;
    private boolean istodo;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStepname() {
        return stepname;
    }

    public void setStepname(String stepname) {
        this.stepname = stepname;
    }

    public boolean istodo() {
        return istodo;
    }

    public void setIstodo(boolean istodo) {
        this.istodo = istodo;
    }
}
