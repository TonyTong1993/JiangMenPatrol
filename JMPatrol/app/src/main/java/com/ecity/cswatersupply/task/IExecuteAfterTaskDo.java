package com.ecity.cswatersupply.task;

import java.io.Serializable;

public interface IExecuteAfterTaskDo {
    void executeOnTaskSuccess(Serializable result);
    void executeTaskError();
    void executeOnTaskFinish();
    
}
