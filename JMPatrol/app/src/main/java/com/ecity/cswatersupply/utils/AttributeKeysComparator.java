package com.ecity.cswatersupply.utils;

import java.util.Comparator;

import com.ecity.cswatersupply.workorder.model.WorkOrderMTField;

/**   
* 此类描述的是：   属性字段排序器
* @author: gaokai
*/

public class AttributeKeysComparator implements Comparator<WorkOrderMTField>{
        
    @Override
    public int compare(WorkOrderMTField left, WorkOrderMTField right) {
        if (left != null && right != null) {
            try {
                if (left.findex < right.findex) {
                    return -1;
                } else {
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }   
}
