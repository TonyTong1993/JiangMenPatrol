package com.ecity.medialibrary.listener;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.activity.FileChooserActivity;
import com.ecity.medialibrary.activity.PlayVideoActivity;
import com.ecity.medialibrary.activity.TakeVideoActivity;
import com.ecity.medialibrary.model.VideoModel;
import com.ecity.medialibrary.utils.FileSuffixUtils;
import com.zzz.ecity.android.applibrary.view.ActionSheet;

public class VideoGridItemClickListener implements OnItemClickListener {
    private String[] menus;
    private Activity activity;
    private List<VideoModel> mVideomodels;

    public VideoGridItemClickListener(Activity activity, List<VideoModel> mVideomodels) {
        this.activity = activity;
        this.mVideomodels = mVideomodels;
        menus = activity.getResources().getStringArray(R.array.custom_action_sheet_video_menus);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showVideoMenu(position);
    }

    private void showVideoMenu(int position) {
        if (position == mVideomodels.size()) {
            if (null == menus || menus.length == 1) {
                Intent takeVideoIntent = new Intent(activity, TakeVideoActivity.class);
                activity.startActivityForResult(takeVideoIntent, MediaRequestCode.TAKE_VIDEOS);
                ActionSheet.dismiss();
            } else {
                ActionSheet.show(activity, activity.getResources().getString(R.string.action_menu_title), menus, new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Intent takeVideoIntent = new Intent(activity, TakeVideoActivity.class);
                                activity.startActivityForResult(takeVideoIntent, MediaRequestCode.TAKE_VIDEOS);
                                ActionSheet.dismiss();
                                break;
                            case 1:
                                FileSuffixUtils.mFileFileterBySuffixs.acceptSuffixs("mp4");
                                Intent selectVideoIntent = new Intent(activity, FileChooserActivity.class);
                                activity.startActivityForResult(selectVideoIntent, MediaRequestCode.SELECT_VIDEOS);
                                ActionSheet.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        } else {
            VideoModel temp = mVideomodels.get(position);
            String path = temp.getPath();
            Intent intent = new Intent(activity, PlayVideoActivity.class);
            intent.putExtra("INTENT_KEY_VIDEOPATH", path);
            intent.putExtra("INTENT_KEY_EDIT", true);
            activity.startActivityForResult(intent, MediaRequestCode.EDIT_VIDEOS);
        }
    }
}
