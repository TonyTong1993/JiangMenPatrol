package com.ecity.cswatersupply.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;

public abstract class BaseFragment extends Fragment {
    
    private CustomTitleView customTitleView; 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(prepareLayoutResource(), null);
        internalInitUI(view);
        return view;
    }

    protected int prepareLayoutResource() {
        return R.layout.fragment_common_list;
    }
  
    protected abstract String getTitle();
    
    private void internalInitUI(View view) {
    	customTitleView = (CustomTitleView)view.findViewById(R.id.customTitleView);
    	customTitleView.setBtnStyle(BtnStyle.NOBTN);
    	customTitleView.setTitleText(getTitle());
    }
}
