package com.ecity.cswatersupply.model;

import android.content.Context;

/**
 * 用于完工上报选择物料操作，可以每个物料的数量。
 * Created by jonathanma on 17/4/2017.
 */

public interface IIncreaseInspectItemModel {
    String generateTitle(Context context);

    String generateDetail(Context context);

    String getCount();

    void setCount(String count);
}
