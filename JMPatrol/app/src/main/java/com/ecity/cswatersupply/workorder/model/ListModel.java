package com.ecity.cswatersupply.workorder.model;

import java.util.ArrayList;
import java.util.List;

import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.model.IListEntity;

/**
 * 列表实体类
 * 
 * @author gaokai
 *
 */
public class ListModel<T> extends AModel implements IListEntity<T> {
    protected List<T> datas;
    private static final long serialVersionUID = 1L;

    @Override
    public List<T> getDatas() {
        if (datas == null) {
            datas = new ArrayList<T>();
        }
        return datas;
    }

    @Override
    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
