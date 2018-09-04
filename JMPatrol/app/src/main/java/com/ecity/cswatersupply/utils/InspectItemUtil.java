package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemSelectValueAdapter;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.z3app.android.util.StringUtil;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

public class InspectItemUtil {

    /**
     * 缓存检查项的回调。
     * @author jonathanma
     *
     */
    public interface ICacheInspectItemsCallback {
        /**
         * @return 缓存的key。要保证唯一性。
         */
        String getCacheKey();

        /**
         * 缓存完成后的回调。
         * @param isSaved 是否保存了内容。
         */
        void onCacheDone(boolean isSaved);
    }

    /**
     * 检查是否有检查项被设了值。
     * @param items
     * @return 任何一个检查项的值不为空，返回true。所有检查项为的值均为空，返回false。
     */
    public static boolean hasValue(List<InspectItem> items) {
        if (ListUtil.isEmpty(items)) {
            return false;
        }

        for (InspectItem inspectItem : items) {
            if (!isValueEmptyOrDefaultValue(inspectItem)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查是否有必填的检查项的值是空的。
     * @param items
     * @return 
     */
    public static boolean hasEmptyItem(List<InspectItem> items) {
        if (ListUtil.isEmpty(items)) {
            return false;
        }

        boolean hasEmptyItem = false;
        for (InspectItem item : items) {
            if (item.isRequired() && item.isVisible() && (StringUtil.isBlank(item.getValue()) || item.getValue().contains("[]"))) {
                hasEmptyItem = true;
                break;
            }
        }

        return hasEmptyItem;
    }

    public static boolean hasVisibleItem(List<InspectItem> items) {
        if (ListUtil.isEmpty(items)) {
            return false;
        }

        boolean hasVisibleItem = false;
        for (InspectItem item : items) {
            if (item.isVisible()) {
                hasVisibleItem = true;
                break;
            }
        }

        return hasVisibleItem;
    }

    /**
     * 创建检查项的缓存json内容。用于保存内容到本地的时候使用。
     * @param inspectItems
     * @return 一个json字符串，里面包含值不为空的检查项的name和value。当没有内容需要缓存时，返回null。
     */
    public static String buildCacheContent(List<InspectItem> inspectItems) {
        if (ListUtil.isEmpty(inspectItems)) {
            return null;
        }

        JSONObject cache = new JSONObject();
        for (InspectItem item : inspectItems) {
            if (isValueEmptyOrDefaultValue(item)) {
                continue;
            }

            try {
                cache.putOpt(item.getName(), item.getValue());
            } catch (JSONException e) {
                LogUtil.e("InspectItemUtil", e);
            }
        }

        return (cache.length() == 0) ? null : cache.toString();
    }

    /**
     * 把类型不是{@link EInspectItemType.GROUP}的检查项合并到一个列表。<br>
     * 如果一个检查项的类型是{@link EInspectItemType.GROUP}，该项的子项会被添加到这个列表，但是该GROUP类型的项不会被添加。<br>
     * 目前的一个使用场景是在保存检查项内容到本地。
     * @param inspectItems
     * @return 包含所有子检查项的列表。
     */
    public static List<InspectItem> mergeAllItemsInline(List<InspectItem> inspectItems) {
        List<InspectItem> allItems = new ArrayList<InspectItem>();
        if (ListUtil.isEmpty(inspectItems)) {
            return allItems;
        }

        for (InspectItem item : inspectItems) {
            if (item.getType() != EInspectItemType.GROUP) {
                allItems.add(item);
                continue;
            }

            List<InspectItem> childItems = item.getChilds();
            if (!ListUtil.isEmpty(childItems)) {
                List<InspectItem> mergedChildren = mergeAllItemsInline(childItems);
                allItems.addAll(mergedChildren);
            }
        }

        return allItems;
    }

    /**
     * 将缓存的值填充到检查项里。
     * @param cacheStr
     */
    /**
     * 将缓存的值填充到检查项里。
     * @param cacheStr 缓存的json。可以还原为JSONObject，key是InspectItem的name，value是InspectItem的值。
     * @param inspectItems
     */
    public static void restoreInspectItemsFromCache(String cacheStr, List<InspectItem> inspectItems) {
        JSONObject cacheJson = JsonUtil.getJsonObject(cacheStr);
        if (cacheJson == null) {
            return;
        }

        List<InspectItem> allItems = InspectItemUtil.mergeAllItemsInline(inspectItems);
        for (InspectItem inspectItem : allItems) {
            String value = cacheJson.optString(inspectItem.getName());
            inspectItem.setValue(value);
        }
    }

    /**
     * 解析检查项的选项。
     * @param inspectItem
     * @return
     */
    public static List<InspectItemSelectValue> parseSelectValues(InspectItem inspectItem) {
        JSONArray jsonArray = JsonUtil.getJsonArray(inspectItem.getSelectValues());
        if (jsonArray == null) {
            return new ArrayList<InspectItemSelectValue>();
        }

        return InspectItemSelectValueAdapter.adapt(jsonArray);
    }

    /**
     * 南昌外勤，解析检查项的选项。
     * @param inspectItem
     * @return
     */
    public static List<InspectItemSelectValue> parseSelectValues1(InspectItem inspectItem) {
        JSONArray jsonArray = JsonUtil.getJsonArray(inspectItem.getSelectValues());
        if (jsonArray == null) {
            return new ArrayList<InspectItemSelectValue>();
        }

        return InspectItemSelectValueAdapter.adapt1(jsonArray);
    }

    /**
     * 提示是否保存检查项。
     * @param context
     * @param inspectItems
     * @param callback
     */
    public static void confirmCacheItems(final Context context, final List<InspectItem> inspectItems, final ICacheInspectItemsCallback callback) {
        String msg = ResourceUtil.getStringById(R.string.save_context);
        AlertView dialog = new AlertView(context, null, msg, new OnAlertViewListener() {
            @Override
            public void back(boolean ok) {
                if (ok) {
                    String cacheContent = InspectItemUtil.buildCacheContent(inspectItems);
                    if (cacheContent != null) {
                        CacheManager.saveObject(context, cacheContent, callback.getCacheKey());
                    }
                } else {
                    CacheManager.deleteCache(context, callback.getCacheKey());
                }
                callback.onCacheDone(ok);
            }
        }, AlertView.AlertStyle.OK_CANCEL_HIGHLIGHT_CANCEL);
        dialog.show();
    }

    private static boolean isValueEmptyOrDefaultValue(InspectItem item) {
        if (StringUtil.isBlank(item.getValue())) {
            return true;
        }

        List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(item);
        if (!ListUtil.isEmpty(selectValues)) {
            if ((item.getType() == EInspectItemType.DROPDOWNLIST) && (item.getValue().equals(selectValues.get(0).gid))) { // 如果是选择类型的第一个值，忽略。因为这是默认选项。
                return true;
            }

            if ((item.getType() == EInspectItemType.TOGGLE) && (item.getValue().equals(selectValues.get(1).gid))) { // TOGGLE默认选否，如果选否，忽略。
                return true;
            }
        }

        return false;
    }

    public static boolean hasGroupItem(List<InspectItem> items) {
        if (ListUtil.isEmpty(items)) {
            return false;
        }

        boolean hasGroupItem = false;
        for (InspectItem item : items) {
            if (EInspectItemType.GROUP == item.getType() ) {
                hasGroupItem = true;
                break;
            }
        }

        return hasGroupItem;
    }
}
