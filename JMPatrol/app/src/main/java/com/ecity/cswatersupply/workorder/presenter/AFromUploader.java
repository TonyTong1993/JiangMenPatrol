package com.ecity.cswatersupply.workorder.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.utils.InspectItemUtil;

import java.util.List;

/**
 * Created by Gxx on 2017/4/12.
 */
public abstract class AFromUploader {
    private Activity activity;

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }

    /***
     * 检查项的完整性检查
     * @param inspectItems
     * @return
     */
    public boolean isInspectItemContentValid(List<InspectItem> inspectItems) {
        if (InspectItemUtil.hasEmptyItem(inspectItems)) {
            Toast.makeText(activity, R.string.is_null_prompt, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public abstract void submit(List<InspectItem> datas);

}
