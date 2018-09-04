package com.ecity.cswatersupply.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.contact.ContactFragment;
import com.ecity.cswatersupply.contact.GenerateDataSource;
import com.ecity.cswatersupply.contact.model.ContactGroup;
import com.ecity.cswatersupply.contact.widght.PathTextView;
import com.ecity.cswatersupply.emergency.EmergencyJsonUtil;
import com.ecity.cswatersupply.emergency.test.Textwriter;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.ui.widght.MapActivityTitleView;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.FileUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/11.
 *
 * 主界面联系人Fragment
 */
public class ContactTabFragment extends Fragment {
    public String cacheContactPath = FileUtil.getInstance(null).getMediaPathforCache() + "contacts.json";
    private MapActivityTitleView titleView;
    private ContactGroup contactGroup;
    private PathTextView title;
    private long level;
    private Map<Long, String> fragmentMap;

    public ContactTabFragment() {
    }

    @Override
    public void onResume() {
        EventBusUtil.register(this);
        super.onResume();
        if(100 == ((MainActivity)getActivity()).getCallResult()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusUtil.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_contact, null);
        titleView = (MapActivityTitleView) view.findViewById(R.id.titleView);
        titleView.setBtnStyle(MapActivityTitleView.BtnStyle.NOBTN);
        titleView.setTitleText(ResourceUtil.getStringById(R.string.fragment_contact_title));
        title = (PathTextView) view.findViewById(R.id.hsv_container);

        initData();
        initUI();
        return view;
    }

    private void initData() {
        fragmentMap = new HashMap<Long, String>();
        JSONObject jsonObject = Textwriter.readJsonString(cacheContactPath);
        if(null == jsonObject) {
            return;
        }
        contactGroup = EmergencyJsonUtil.parseContactsModel(jsonObject);
        if(null == contactGroup) {
            return;
        }
        level = 0;
        title.clearRoot();
        title.initRoot(contactGroup.getGroupName(), Color.parseColor("#48a0c7"));
        title.setOnItemClickListener(new OnTitleClickListener());
    }

    private void initUI() {
        if(null == contactGroup) {
            return;
        }

        FragmentManager manager = getActivity().getSupportFragmentManager() ;
        FragmentTransaction transaction = manager.beginTransaction();
        ContactFragment contactFragment = new ContactFragment(contactGroup);
        level++;
        fragmentMap.put(level, contactGroup.getGroupName());

        transaction.add(R.id.fragment_container, contactFragment);
        transaction.addToBackStack(contactGroup.getGroupName());
        transaction.commit();
    }

    public void onEventMainThread(UIEvent event) {
        switch (event.getId()) {
            case UIEventStatus.EMERGENCY_CONTACT_GET_CONTACTS:
                ContactGroup contactGroup = event.getData();
                if(null != contactGroup) {
                    this.contactGroup = contactGroup;
                }
                level--;
                initData();
                initUI();
                break;
            case UIEventStatus.EMERGENCY_CONTACT_PUSH_STACK:
                ContactGroup contactGroup1 = event.getData();
                level++;
                if(null != contactGroup1.getChildGroup()) {
                    title.append(level, Color.parseColor("#48a0c7"), contactGroup1.getGroupName());

                } else {
                    title.append(level, Color.parseColor("#cccccc"), contactGroup1.getGroupName());
                }
                fragmentMap.put(level,contactGroup1.getGroupName());
                break;
            default:
                break;
        }
    }

    /***
     * 点击中间路径时回调
     */
    private class OnTitleClickListener implements PathTextView.OnItemClickListener{
        @Override
        public void onClick(long currentId, int backCount) {
            String name;
            if(-1 == currentId) {
                name = fragmentMap.get(Long.valueOf(2));
                fragmentMap.remove(Long.valueOf(2));
                level = 1;
            } else {
                name = fragmentMap.get(currentId + 1);
                fragmentMap.remove(currentId + 1);
                level = currentId;
            }
            getFragmentManager().popBackStack(name, -1);
        }
    }

}
