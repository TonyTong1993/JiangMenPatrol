package com.ecity.android.contactmanchooser;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import com.ecity.android.contactmanchooser.model.ContactMan;

/**
 * 工作核心
 * 
 * @author gaokai
 *
 */
public abstract class AContactManCore {
    private WeakReference<ChooseContactManActivity> mActivity;

    public ChooseContactManActivity getActivity() {
        if (null == mActivity) {
            return null;
        }
        ChooseContactManActivity activity = mActivity.get();
        return activity;
    }

    public abstract List<ContactMan> getContactMen(Map<String, String> params,String requestType);

    public void beforeFinishActivity() {

    };

    final void register(ChooseContactManActivity activity) {
        mActivity = new WeakReference<ChooseContactManActivity>(activity);
    }

}
