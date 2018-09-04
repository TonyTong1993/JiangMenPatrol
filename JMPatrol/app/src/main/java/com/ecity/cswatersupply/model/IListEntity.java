package com.ecity.cswatersupply.model;

import java.io.Serializable;
import java.util.List;

public interface IListEntity<T> extends Serializable {
    
    List<T> getDatas();

    void setDatas(List<T> datas);
}
