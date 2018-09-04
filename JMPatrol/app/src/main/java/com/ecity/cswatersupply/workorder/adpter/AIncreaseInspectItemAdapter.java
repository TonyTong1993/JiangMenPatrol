package com.ecity.cswatersupply.workorder.adpter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.model.IIncreaseInspectItemModel;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.zzz.ecity.android.applibrary.dialog.AlertView;

import java.util.ArrayList;

/**
 * Created by jonathanma on 17/4/2017.
 */
public abstract class AIncreaseInspectItemAdapter<T extends IIncreaseInspectItemModel> extends ArrayListAdapter<T> {

    public interface OnItemCountChangedListener<T> {
        void onItemCountChanged(T item);
        void onItemRemoved(T item);
    }

    private Activity mActivity;
    private LayoutInflater mInflater;
    private OnItemCountChangedListener listener;

    public AIncreaseInspectItemAdapter(Activity activity, InspectItem parentItem) {
        super(activity);
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.mList = new ArrayList<T>();
    }

    protected abstract void deselectItem(int position);
    protected abstract int getItemMinCount();

    public void setOnItemCountChangedListener(OnItemCountChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IIncreaseInspectItemModel item = mList.get(position);

        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.custom_form_subitem_one_increase_add_minus, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String title = item.generateTitle(mActivity);
        viewHolder.tvIncreaseLeft.setText(title);
        viewHolder.tvAdd.setOnClickListener(new MyAddMinusOnClickListener(viewHolder, position));
        viewHolder.tvMinus.setOnClickListener(new MyAddMinusOnClickListener(viewHolder, position));
        viewHolder.llContainer.setOnLongClickListener(new MyAddListViewOnItemLongClickListener(position));
        viewHolder.etValue.addTextChangedListener(new MyEtValueTextWatcher(viewHolder));
        viewHolder.etValue.setTag(position);

        String count = item.getCount();
        if (StringUtil.isBlank(count)) {
            count = String.valueOf(getItemMinCount());
        }
        viewHolder.etValue.setText(count);

        return convertView;
    }

    @Override
    public int getCount() {
        return (mList == null) ? 0 : mList.size();
    }

    private void removeItem(int position) {
        IIncreaseInspectItemModel item = mList.get(position);

        deselectItem(position);
        mList.remove(position);
        notifyDataSetChanged();
        if (listener != null) {
            listener.onItemRemoved(item);
        }
    }

    private class MyAddMinusOnClickListener implements View.OnClickListener {
        private ViewHolder viewHolder;
        private int position;

        public MyAddMinusOnClickListener(ViewHolder viewHolder, int position) {
            this.viewHolder = viewHolder;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            float value;
            try {
                value = Float.valueOf(viewHolder.etValue.getText().toString());
            } catch (NumberFormatException e) {
                value = getItemMinCount();
            }

            switch (v.getId()) {
                case R.id.tv_add:
                    value++;
                    viewHolder.etValue.setText(String.valueOf(value));
                    break;
                case R.id.tv_minus:
                    if (value <= getItemMinCount()) {
                        removeItem(position);
                    } else {
                        value--;
                        viewHolder.etValue.setText(String.valueOf(value));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class MyEtValueTextWatcher implements TextWatcher {
        private ViewHolder holderFirst;

        public MyEtValueTextWatcher(ViewHolder holderFirst) {
            this.holderFirst = holderFirst;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int position = (Integer) holderFirst.etValue.getTag();
            mList.get(position).setCount(s.toString());
            onItemCountChanged(position);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class MyAddListViewOnItemLongClickListener implements View.OnLongClickListener {
        private int position;

        public MyAddListViewOnItemLongClickListener(int pos) {
            this.position = pos;
        }

        @Override
        public boolean onLongClick(View v) {
            AlertView dialog = new AlertView(mActivity, mActivity.getString(R.string.dialog_title_prompt), mActivity.getString(R.string.event_report_delete_item_tips),
                    new AlertView.OnAlertViewListener() {

                        @Override
                        public void back(boolean result) {
                            if (result) {
                                removeItem(position);
                            }
                        }
                    }, AlertView.AlertStyle.OK_CANCEL);
            dialog.show();

            return true;
        }
    }

    private void onItemCountChanged(int position) {
        if (listener != null) {
            listener.onItemCountChanged(mList.get(position));
        }
    }

    private static class ViewHolder {
        private LinearLayout llContainer;
        private TextView tvIncreaseLeft;
        private TextView tvAdd;
        private TextView tvMinus;
        private EditText etValue;

        public ViewHolder(View convertView) {
            llContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
            tvIncreaseLeft = (TextView) convertView.findViewById(R.id.tv_increase_value_left);
            tvAdd = (TextView) convertView.findViewById(R.id.tv_add);
            tvMinus = (TextView) convertView.findViewById(R.id.tv_minus);
            etValue = (EditText) convertView.findViewById(R.id.et_value);
        }
    }
}
