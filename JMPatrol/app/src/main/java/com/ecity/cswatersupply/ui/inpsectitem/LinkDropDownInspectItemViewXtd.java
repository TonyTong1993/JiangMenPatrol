package com.ecity.cswatersupply.ui.inpsectitem;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

import java.util.List;

public class LinkDropDownInspectItemViewXtd extends ABaseInspectItemView {

    private TextView tvStar;
    private TextView tvTitle;
    private EditText tvValue;
    private TextView viewTitle;
    private String spnValue;
    private List<InspectItemSelectValue> selectValueLists;
    private String txtValue = "";

    @Override
    protected void setup(View contentView) {
        Spinner spnSelect = (Spinner) contentView.findViewById(R.id.spn_value);
        ImageView ivSpnValue = (ImageView) contentView.findViewById(R.id.iv_spn_value);
        tvStar = (TextView) contentView.findViewById(R.id.ll_text).findViewById(R.id.tv_item_star);
        tvTitle = (TextView) contentView.findViewById(R.id.ll_text).findViewById(R.id.tv_item_title);
        tvTitle.setText(mInspectItem.getLinkAlias());
        tvValue = (EditText) contentView.findViewById(R.id.ll_text).findViewById(R.id.et_item_value);
        viewTitle = (TextView) contentView.findViewById(R.id.tv_item_common_title);
        viewTitle.setText(mInspectItem.getGroupName());
        selectValueLists = InspectItemUtil.parseSelectValues(mInspectItem);
        String[] spnDatas = {};

        if (!ListUtil.isEmpty(selectValueLists)) {
            spnDatas = new String[selectValueLists.size()];
            for (int i = 0; i < selectValueLists.size(); i++) {
                spnDatas[i] = selectValueLists.get(i).name;
            }
            ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(context, R.layout.custom_form_item_spinner_textview, spnDatas);
            spnAdapter.setDropDownViewResource(R.layout.custom_form_item_spinner_dropdown);
            spnSelect.setAdapter(spnAdapter);
        }
        spnSelect.setPrompt(context.getString(R.string.please_select));
        spnSelect.setOnItemSelectedListener(new MySpinnerOnItemSelectedClick(mInspectItem));
        ivSpnValue.setOnClickListener(new MyImgSpnViewOnClickListener(spnSelect));
        tvValue.addTextChangedListener(new MyEditTextListener(mInspectItem));

        if (!mInspectItem.getValue().isEmpty()) {
            String dropValue = "";
            String value = mInspectItem.getValue();
            if(value.contains(",")) {
                String[] strs = value.split(",");
                if(null != strs && 2 == strs.length) {
                    dropValue = strs[0];
                    txtValue = strs[1];
                    for (int i = 0; i < selectValueLists.size(); i++) {
                        String gid = getGidByName(dropValue);
                        if (selectValueLists.get(i).gid.equals(gid)) {
                            spnSelect.setSelection(i);
                        }
                    }
                    tvValue.setText(txtValue);
                }
            }
        }
    }

    private String getGidByName(String dropValue) {
        if(ListUtil.isEmpty(selectValueLists)) {
            return "";
        }
        for(InspectItemSelectValue selectValue : selectValueLists) {
            if(dropValue.equals(selectValue.getName())) {
                return selectValue.getGid();
            }
        }

        return "";
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_link_spinner;
    }

    private class MySpinnerOnItemSelectedClick implements OnItemSelectedListener {
        private InspectItem item;
        private String[] gids;
        private String[] names;

        public MySpinnerOnItemSelectedClick(InspectItem item) {
            this.item = item;
            List<InspectItemSelectValue> selectValues = InspectItemUtil.parseSelectValues(item);
            names = new String[selectValues.size()];
            gids = new String[selectValues.size()];
            for (int i = 0; i < selectValues.size(); i++) {
                gids[i] = selectValues.get(i).gid;
                names[i] = selectValues.get(i).name;
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spnValue = gids[position];
            if(gids[position].equals("90080001")) {
                tvStar.setVisibility(View.GONE);
                tvValue.setText(ResourceUtil.getStringById(R.string.item_empty));
                tvValue.setEnabled(false);
            } else {
                tvStar.setVisibility(View.VISIBLE);
                //有默认值时加载默认值，没有默认值时，联动的时候该项值清空
                if(!StringUtil.isBlank(txtValue)) {
                    tvValue.setText(txtValue);
                } else {
                    tvValue.setText("");
                }
                tvValue.setEnabled(true);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    private class MyImgSpnViewOnClickListener implements OnClickListener {
        private Spinner spinner;

        public MyImgSpnViewOnClickListener(Spinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void onClick(View v) {
            if (null != spinner) {
                spinner.setVisibility(View.VISIBLE);
                spinner.performClick();
            }
        }
    }

    private class MyEditTextListener implements TextWatcher {
        private InspectItem item;

        public MyEditTextListener(InspectItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no logic to do.
        }

        @Override
        public void afterTextChanged(Editable s) {
            String itemValue = spnValue + "," + s.toString();
            item.setValue(itemValue);
        }
    }
}
