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

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.model.checkitem.InspectItemSelectValue;
import com.ecity.cswatersupply.utils.InspectItemUtil;
import com.ecity.cswatersupply.utils.ListUtil;

import java.util.List;

public class DropDownInspectItemViewXtd extends ABaseInspectItemView {
    private List<InspectItemSelectValue> selectValueLists;

    @Override
    protected void setup(View contentView) {
        boolean isDropDownListType = mInspectItem.getType().equals(EInspectItemType.DROPDOWNLIST);

        Spinner spnSelect = (Spinner) contentView.findViewById(R.id.spn_value);
        EditText etSpnValue = (EditText) contentView.findViewById(R.id.et_spn_value);
        etSpnValue.setVisibility(View.GONE);
        ImageView ivSpnValue = (ImageView) contentView.findViewById(R.id.iv_spn_value);

        selectValueLists = InspectItemUtil.parseSelectValues(mInspectItem);
        String[] spnDatas = {};
        if (!isDropDownListType) {
            if (!ListUtil.isEmpty(selectValueLists)) {
                spnDatas = new String[selectValueLists.size() + 1];
                for (int i = 0; i < selectValueLists.size(); i++) {
                    spnDatas[i] = selectValueLists.get(i).name;
                }
                spnDatas[selectValueLists.size()] = context.getString(R.string.event_report_other);
                ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(context, R.layout.custom_form_item_spinner_textview, spnDatas);
                spnAdapter.setDropDownViewResource(R.layout.custom_form_item_spinner_dropdown);
                spnSelect.setAdapter(spnAdapter);
                if (!mInspectItem.getValue().isEmpty()) {
                    boolean hasValue = false;
                    for (int i = 0; i < selectValueLists.size(); i++) {
                        if (selectValueLists.get(i).gid.equals(mInspectItem.getValue())) {
                            hasValue = true;
                            spnSelect.setSelection(i);
                        }
                    }
                    if (!hasValue) {
                        etSpnValue.setText(mInspectItem.getValue());
                        etSpnValue.setVisibility(View.VISIBLE);
                        spnSelect.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            if (!ListUtil.isEmpty(selectValueLists)) {
                spnDatas = new String[selectValueLists.size()];
                for (int i = 0; i < selectValueLists.size(); i++) {
                    spnDatas[i] = selectValueLists.get(i).name;
                }

                ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(context, R.layout.custom_form_item_spinner_textview, spnDatas);
                spnAdapter.setDropDownViewResource(R.layout.custom_form_item_spinner_dropdown);
                spnSelect.setAdapter(spnAdapter);
                if (mInspectItem.getValue() != null && !mInspectItem.getValue().isEmpty()) {
                    for (int i = 0; i < selectValueLists.size(); i++) {
                        if (selectValueLists.get(i).gid.equals(mInspectItem.getValue())) {
                            spnSelect.setSelection(i);
                        }
                    }
                }
            }
        }

        spnSelect.setPrompt(context.getString(R.string.please_select));
        spnSelect.setOnItemSelectedListener(new MySpinnerOnItemSelectedClick(mInspectItem, spnSelect, etSpnValue, isDropDownListType));
        ivSpnValue.setOnClickListener(new MyImgSpnViewOnClickListener(spnSelect));
        etSpnValue.addTextChangedListener(new MyEditTextListener(mInspectItem));

        if (!mInspectItem.isEdit()) {
            etSpnValue.setEnabled(false);
            etSpnValue.setVisibility(View.VISIBLE);
            etSpnValue.setText(mInspectItem.getValue());
            spnSelect.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.custom_form_item_spinner;
    }

    private class MySpinnerOnItemSelectedClick implements OnItemSelectedListener {
        private InspectItem item;
        private String[] gids;
        private String[] names;
        private Spinner spinner;
        private EditText etText;
        private boolean isDdlNormal;

        public MySpinnerOnItemSelectedClick(InspectItem item, Spinner spinner, EditText editText, boolean isDdlNormal) {
            this.item = item;
            this.spinner = spinner;
            this.etText = editText;
            this.isDdlNormal = isDdlNormal;

            names = new String[selectValueLists.size()];
            gids = new String[selectValueLists.size()];
            for (int i = 0; i < selectValueLists.size(); i++) {
                gids[i] = selectValueLists.get(i).gid;
                names[i] = selectValueLists.get(i).name;
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (!isDdlNormal) {
                if (position == gids.length) {
                    spinner.setVisibility(View.GONE);
                    etText.setVisibility(View.VISIBLE);
                } else {
                    etText.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    item.setValue(gids[position]);
                }
            } else {
                item.setValue(gids[position]);
                if ((null != mInspectItem.getCascadeGroupName()) && (("0").equals(mInspectItem.getCascadeGroupSn()))) {
                    UIEvent event = new UIEvent(UIEventStatus.REFRESH_CASCAED_INSPECTITEM, mInspectItem);
                    EventBusUtil.post(event);
                }
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
            item.setValue(s.toString());
        }
    }
}
