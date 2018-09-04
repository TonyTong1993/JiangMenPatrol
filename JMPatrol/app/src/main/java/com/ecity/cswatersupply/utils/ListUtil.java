package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtil {
    public static void clearList(List<?> list) {
        if (list != null) {
            list.clear();
        }
    }

    /**
     * Check if list is null or zero-size.
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return (list == null) || (list.size() == 0);
    }

    /**
     * Check if array is null or zero-length.
     * @param array
     * @return
     */
    public static <T> boolean isEmpty(T[] array) {
        return (array == null) || (array.length == 0);
    }

    /**
     * If list is not null, return list itself. Otherwise return a new ArrayList.
     * @param list
     * @return
     */
    public static <T> List<T> getSafeList(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }

        return list;
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list, boolean isAscending) {
        Collections.sort(list);
        if (!isAscending) {
            Collections.reverse(list);
        }
    }

    /**
     * Convert an array to List.
     * @param array
     * @return
     */
    public static <T> List<T> toList(T[] array) {
        return Arrays.asList(array);
    }

    /**
     * Convert an array to List.
     * 
     * @param list source
     * @param target An empty array to put the elements. Can not be null.
     * @return An array with elements from list
     */
    public static <T> T[] toArray(List<T> list, T[] target) {
        return list.toArray(target);
    }
}
