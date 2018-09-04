package com.ecity.medialibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.adapter.ImageBucketAdapter;
import com.ecity.medialibrary.model.ImageBucket;
import com.ecity.medialibrary.utils.AlbumHelper;
import com.zzz.ecity.android.applibrary.utils.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/****
 *
 * Description:相册照片选择器<br/>
 *
 * @version V1.0
 * @Author ZiZhengzhuan
 * @CreateDate 2016年1月12日
 * @email
 */
public class AlbumSelectActivity extends Activity {
    /**
     * 设置允许选择的相册名称。设置后，只有这些相册才会显示，供选择。不设置的话，显示所有可用的相册。
     */
    public static final String INTENT_KEY_PREFERRED_ALBUM_NAMES = "INTENT_KEY_PREFERRED_ALBUM_NAMES";
    public static final String INTENT_KEY_TAKE_PHOTO_IMAGE_NAMES = "takePhotoImageNames";
    List<ImageBucket> dataList;
    GridView gridView;
    ImageBucketAdapter adapter;
    AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bimap;
    private Button btcancel;
    private List<String> preferredAlbumNames;
    private ArrayList<String> takePhotoImageNames = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_image_bucket);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        initData();
        initView();
    }

    /***
     * *
     * Description:初始化数据<br/>
     * @version V1.0
     */
    private void initData() {
        preferredAlbumNames = getIntent().getStringArrayListExtra(INTENT_KEY_PREFERRED_ALBUM_NAMES);
        takePhotoImageNames = getIntent().getStringArrayListExtra(INTENT_KEY_TAKE_PHOTO_IMAGE_NAMES);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
        List<ImageBucket> buckets = helper.getImagesBucketList(true);
        if (ListUtil.isEmpty(buckets) || ListUtil.isEmpty(preferredAlbumNames)) {
            dataList = buckets;
        } else {
            dataList = new ArrayList<ImageBucket>();
            for (ImageBucket bucket : buckets) {
                if (preferredAlbumNames.contains(bucket.bucketName)) {
                    dataList.add(bucket);
                }
            }
        }
    }

    /***
     * *
     * Description:初始化视图控件<br/>
     * @version V1.0
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(AlbumSelectActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ImageGridActivity.setIBtnConfirmOnClickCallback(mIOnClickBtnConfirmCallback);
                Intent intent = new Intent(AlbumSelectActivity.this, ImageGridActivity.class);
                intent.putStringArrayListExtra(INTENT_KEY_TAKE_PHOTO_IMAGE_NAMES, takePhotoImageNames);
                intent.putExtra(AlbumSelectActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
                startActivity(intent);
                //finish();
            }
        });

        btcancel = (Button) findViewById(R.id.btcancel);
        if (null == btcancel)
            return;
        btcancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private ImageGridActivity.IBtnConfirmOnClickCallback mIOnClickBtnConfirmCallback = new ImageGridActivity.IBtnConfirmOnClickCallback() {
        @Override
        public void OnBtnConfirmClicked() {
            AlbumSelectActivity.this.finish();
        }
    };
}
