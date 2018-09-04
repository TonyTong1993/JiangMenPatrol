package com.ecity.cswatersupply.workorder.adpter;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.workorder.network.WorkorderLeaderModel;
import com.ecity.cswatersupply.workorder.view.MyHScrollView;

import java.util.List;

public class WorkOrderLeaderTableAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<WorkorderLeaderModel> lists;
    private Activity context;
    private RelativeLayout head;

    public WorkOrderLeaderTableAdapter(Activity context, List<WorkorderLeaderModel> lists, RelativeLayout head) {
        super();
        this.lists = lists;
        this.context = context;
        this.head = head;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            synchronized (context) {
                convertView = inflater.inflate(R.layout.horlistview_item, null);
                holder = new ViewHolder();

                MyHScrollView scrollView1 = (MyHScrollView) convertView
                        .findViewById(R.id.horizontalScrollView1);

                holder.scrollView = scrollView1;
                holder.tv_wxry = (TextView) convertView.findViewById(R.id.text_view1);
                holder.tv_zds = (TextView) convertView.findViewById(R.id.text_view2);
                holder.tv_djd = (TextView) convertView.findViewById(R.id.text_view3);
                holder.tv_clz = (TextView) convertView.findViewById(R.id.text_view4);
                holder.tv_ywj = (TextView) convertView.findViewById(R.id.text_view5);
                holder.tv_yq = (TextView) convertView.findViewById(R.id.text_view6);
                holder.tv_cq = (TextView) convertView.findViewById(R.id.text_view7);
                holder.tv_zd = (TextView) convertView.findViewById(R.id.text_view8);
                holder.tv_td = (TextView) convertView.findViewById(R.id.text_view9);
                holder.tv_gs = (TextView) convertView.findViewById(R.id.text_view10);

                MyHScrollView headSrcrollView = (MyHScrollView) head
                        .findViewById(R.id.horizontalScrollView1);
                headSrcrollView
                        .AddOnScrollChangedListener(new OnScrollChangedListenerImp(
                                scrollView1));

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WorkorderLeaderModel model = lists.get(position);
        if (model.getWxry().isEmpty()) {
            holder.tv_wxry.setText(R.string.summary_over_total);
        } else {
            holder.tv_wxry.setText(model.getWxry());
        }
        if (model.getZds().isEmpty()) {
            model.setZds("0");
        }
        if (model.getJdd().isEmpty()) {
            model.setJdd("0");
        }
        if (model.getClz().isEmpty()) {
            model.setClz("0");
        }
        if (model.getYwj().isEmpty()) {
            model.setYwj("0");
        }
        if (model.getYq().isEmpty()) {
            model.setYq("0");
        }
        if (model.getCq().isEmpty()) {
            model.setCq("0");
        }
        if (model.getZd().isEmpty()) {
            model.setZd("0");
        }
        if (model.getTd().isEmpty()) {
            model.setTd("0");
        }
        if (model.getGs().isEmpty()) {
            model.setGs("0");
        }
        holder.tv_zds.setText(model.getZds());
        holder.tv_djd.setText(model.getJdd());
        holder.tv_clz.setText(model.getClz());
        holder.tv_ywj.setText(model.getYwj());
        holder.tv_yq.setText(model.getYq());
        holder.tv_cq.setText(model.getCq());
        holder.tv_zd.setText(model.getZd());
        holder.tv_td.setText(model.getTd());
        holder.tv_gs.setText(model.getGs());

        holder.tv_zds.setTextColor(Color.BLACK);
        holder.tv_djd.setTextColor(Color.BLACK);
        holder.tv_clz.setTextColor(Color.BLACK);
        holder.tv_ywj.setTextColor(Color.BLACK);
        holder.tv_yq.setTextColor(Color.BLACK);
        holder.tv_cq.setTextColor(Color.BLACK);
        holder.tv_zd.setTextColor(Color.BLACK);
        holder.tv_td.setTextColor(Color.BLACK);
        holder.tv_gs.setTextColor(Color.BLACK);
        holder.tv_wxry.setTextColor(Color.BLACK);
        TextPaint tp = holder.tv_wxry.getPaint();
        TextPaint zds = holder.tv_zds.getPaint();
        TextPaint djd = holder.tv_djd.getPaint();
        TextPaint clz = holder.tv_clz.getPaint();
        TextPaint ywj = holder.tv_ywj.getPaint();
        TextPaint yq = holder.tv_yq.getPaint();
        TextPaint cq = holder.tv_cq.getPaint();
        TextPaint zd = holder.tv_zd.getPaint();
        TextPaint td = holder.tv_td.getPaint();
        if (lists.get(0) == model) {
            tp.setFakeBoldText(false);
            zds.setFakeBoldText(false);
            djd.setFakeBoldText(false);
            clz.setFakeBoldText(false);
            ywj.setFakeBoldText(false);
            yq.setFakeBoldText(false);
            cq.setFakeBoldText(false);
            zd.setFakeBoldText(false);
            td.setFakeBoldText(false);
        } else if (lists.get(lists.size() - 1) == model) {
            tp.setFakeBoldText(true);
            zds.setFakeBoldText(true);
            djd.setFakeBoldText(true);
            clz.setFakeBoldText(true);
            ywj.setFakeBoldText(true);
            yq.setFakeBoldText(true);
            cq.setFakeBoldText(true);
            zd.setFakeBoldText(true);
            td.setFakeBoldText(true);
        }

        if (position % 2 != 0) {
            convertView.setBackgroundColor(Color.argb(250, 255, 255, 255));
        } else {
            convertView.setBackgroundColor(Color.argb(250, 224, 243, 250));
        }

        return convertView;
    }

    class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
        MyHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }

    class ViewHolder {
        TextView tv_wxry;
        TextView tv_zds;
        TextView tv_djd;
        TextView tv_clz;
        TextView tv_ywj;
        TextView tv_yq;
        TextView tv_cq;
        TextView tv_zd;
        TextView tv_td;
        TextView tv_gs;
        HorizontalScrollView scrollView;
    }
}
