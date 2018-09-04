package com.ecity.android.map.core.util;

import java.util.List;

public class ListUtil {
    public static boolean isEmpty(List<?> list) {
        if ((list == null) || (list.size() == 0)) {
            return true;
        }
        return false;
    }
}
