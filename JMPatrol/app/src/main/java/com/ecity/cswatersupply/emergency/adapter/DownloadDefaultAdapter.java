package com.ecity.cswatersupply.emergency.adapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.emergency.model.EmergencyPlanModel;
import com.ecity.cswatersupply.emergency.utils.UniversalImageLoaderUtil;
import com.ecity.medialibrary.widght.TouchImageView;
import com.z3app.android.util.StringUtil;

public class DownloadDefaultAdapter extends BaseAdapter {
    private List<EmergencyPlanModel> dataSource = new ArrayList<EmergencyPlanModel>();
    private LayoutInflater mInflater;
    private Context context;
    private IDownloadItemClickListener<EmergencyPlanModel> mListener;

    public DownloadDefaultAdapter(Context context, List<EmergencyPlanModel> dataSource) {
        mInflater = LayoutInflater.from(context);
        this.dataSource = dataSource;
        this.context = context;
    }

    public void setOnItemClickListener(IDownloadItemClickListener<EmergencyPlanModel> listener) {
        this.mListener = listener;
    }

    public void updateListView(List<EmergencyPlanModel> dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_download_default, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_redownload = (TextView) convertView.findViewById(R.id.tv_redownload);
            viewHolder.tv_emergency_name = (TextView) convertView.findViewById(R.id.tv_emergency_name);
            viewHolder.tv_emergency_updater = (TextView) convertView.findViewById(R.id.tv_emergency_updater);
            viewHolder.tv_emergency_updatetime = (TextView) convertView.findViewById(R.id.tv_emergency_updatetime);
            viewHolder.prg_downloadstate = (NumberProgressBar) convertView.findViewById(R.id.prg_downloadstate);
            viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            viewHolder.image = (TouchImageView) convertView.findViewById(R.id.image);
            viewHolder.tv_emergency_describe = (TextView) convertView.findViewById(R.id.tv_emergency_describe);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setData(viewHolder, position);
        return convertView;
    }

    /**
     * 设置viewHolder的数据
     * 
     * @param holder
     * @param itemIndex
     */
    private void setData(ViewHolder holder, final int itemIndex) {
        final EmergencyPlanModel model = dataSource.get(itemIndex);
        holder.tv_emergency_name.setText(StringUtil.isBlank(model.getName())?model.getDoc():model.getName());
        holder.tv_emergency_updater.setText(model.getUploader());
        holder.tv_emergency_updatetime.setText(model.getUploadTime());
        holder.tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, itemIndex, model,false);
                }
            }
        });
        
        holder.tv_redownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, itemIndex, model,true);
                }
            }
        });
        
        holder.prg_downloadstate.setProgress(model.getProgress());
        
        if(!StringUtil.isBlank(model.getThumbnail())){
            UniversalImageLoaderUtil.loadImage(context, model.getThumbnail(), holder.image); 
        } else {
            holder.image.setImageResource(getResuorceId(model.getDoc()));
        }
        
        if(!StringUtil.isBlank(model.getDescribe())){
            holder.tv_emergency_describe.setText(model.getDescribe());
        }
        
        updateButtonByStatus(holder, model.getStatus());
    }
    
    private int getResuorceId(String docName){
        if(StringUtil.isBlank(docName)){
            return R.drawable.ic_empty;
        }
        File file = new File(docName);
        docName = docName.toLowerCase();
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
                || end.equals("wav")) {
            return R.drawable.icon_doc_video;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return R.drawable.icon_doc_video;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg")
                || end.equals("bmp")) {
            return R.drawable.icon_doc_image;
        } else if (end.equals("apk")) {
            return R.drawable.icon_doc_app;
        } else if (end.equals("ppt") || end.equals("pptx")) {
            return R.drawable.icon_doc_ppt;
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return R.drawable.icon_doc_excel;
        } else if (end.equals("doc") || end.equals("docx")) {
            return R.drawable.icon_doc_word;
        } else if (end.equals("pdf")) {
            return R.drawable.icon_doc_pdf;
        } else if (end.equals("chm")) {
            return R.drawable.icon_doc_pdf;
        } else if (end.equals("txt")) {
            return R.drawable.icon_doc_txt;
        } else {
            return R.drawable.ic_empty;
        }
    }

    /**
     * 局部刷新
     * 
     * @param view
     * @param itemIndex
     */
    public void updateView(View view, int itemIndex) {
        if (view == null) {
            return;
        }
        // 从view中取得holder
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tv_redownload = (TextView) view.findViewById(R.id.tv_redownload);
        holder.tv_emergency_name = (TextView) view.findViewById(R.id.tv_emergency_name);
        holder.tv_emergency_updater = (TextView) view.findViewById(R.id.tv_emergency_updater);
        holder.tv_emergency_updatetime = (TextView) view.findViewById(R.id.tv_emergency_updatetime);
        holder.prg_downloadstate = (NumberProgressBar) view.findViewById(R.id.prg_downloadstate);
        holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
        holder.image = (TouchImageView) view.findViewById(R.id.image);
        holder.tv_emergency_describe = (TextView) view.findViewById(R.id.tv_emergency_describe);
        setData(holder, itemIndex);
    }

    /**
     * 根据状态设置图标
     * 
     * @param downloadPercentView
     * @param status
     */
    private void updateButtonByStatus(ViewHolder holder, int status) {
        holder.tv_state.setVisibility(View.VISIBLE);
        holder.tv_redownload.setVisibility(View.GONE);
        holder.prg_downloadstate.setVisibility(View.VISIBLE);
        switch (status) {
        case EmergencyPlanModel.STATUS_NOT_DOWNLOAD:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_download));
            break;
        case EmergencyPlanModel.STATUS_CONNECTING:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_orange);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_waiting));
            break;
        case EmergencyPlanModel.STATUS_CONNECT_ERROR:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_download));
            break;
        case EmergencyPlanModel.STATUS_DOWNLOADING:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_downloading));
            break;
        case EmergencyPlanModel.STATUS_PAUSED:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_orange);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_puased));
            break;
        case EmergencyPlanModel.STATUS_DOWNLOAD_ERROR:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_download));
            break;
        case EmergencyPlanModel.STATUS_COMPLETE:
            //holder.tv_state.setBackgroundResource(R.drawable.css_button_blue);
            holder.prg_downloadstate.setVisibility(View.GONE);
            holder.tv_redownload.setVisibility(View.VISIBLE);
            holder.tv_state.setText(context.getResources().getString(R.string.str_emergency_downloaded));
            break;
        default:
            break;
        }
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public EmergencyPlanModel getItem(int position) {
        if (null == dataSource)
            return null;
        return dataSource.get(position);
    }

    @Override
    public int getCount() {
        if (null == dataSource) {
            return 0;
        }
        return dataSource.size();
    }

    public final static class ViewHolder {
        public TextView tv_emergency_name;
        public TextView tv_emergency_updater;
        public TextView tv_emergency_updatetime;
        public NumberProgressBar prg_downloadstate;
        public TextView tv_state;
        public TextView tv_redownload;
        public TouchImageView image;
        public TextView tv_emergency_describe;
    }
}