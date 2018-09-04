/*
 * Copyright (C) 2013 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ecity.medialibrary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.fragment.FileListFragment;
import com.ecity.medialibrary.model.FileModel;
import com.ecity.medialibrary.utils.MediaCacheManager;
import com.zzz.ecity.android.applibrary.view.TitleView;
import com.zzz.ecity.android.applibrary.view.TitleView.BtnStyle;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ecity.medialibrary.fragment.FileListFragment.MAX_FILE_COUNT;
import static com.ecity.medialibrary.fragment.FileListFragment.SELECT_FILE_TYPE;

/**
 * Main Activity that handles the FileListFragments
 *
 * @version 2016-05-17
 * @author SunShan'ai 
 */
public class FileChooserActivity extends FragmentActivity implements OnBackStackChangedListener, FileListFragment.Callbacks {
    public interface ITitleBarCallBack {
        public void onLeftButtonClicked(View view);

        public void onRightButtonClicked(View view);
    }

    public static final String PATH = "path";
    public static final String PATHS = "paths";
    public static final String EXTERNAL_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ITitleBarCallBack callBack;
    private TitleView mViewTitle;
    private TextView mBtnDone;
    private int selectedFileCount = 0;
    private int maxSelectFileCount;
    private String selectFileType;
    private static final boolean HAS_ACTIONBAR = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private FragmentManager mFragmentManager;
    private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.storage_removed, Toast.LENGTH_LONG).show();
            finishWithResults(null);
        }
    };

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediafile_chooser);
        initActivityTitleUI();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);
        this.maxSelectFileCount = getIntent().getExtras().getInt(MAX_FILE_COUNT);
        this.selectFileType = getIntent().getExtras().getString(SELECT_FILE_TYPE);

        if (savedInstanceState == null) {
            mPath = EXTERNAL_BASE_PATH;
            addFragment();
        } else {
            mPath = savedInstanceState.getString(PATH);
        }

//        setTitle(mPath);

        selectedFileCount = MediaCacheManager.attachdrr.size();
        if("select".equals(selectFileType)) {
            mBtnDone.setText(getResources().getString(R.string.str_down) + "(" + selectedFileCount + "/" + maxSelectFileCount + ")");
        } else {
            mBtnDone.setText(getResources().getString(R.string.str_down) + "(" + 0 + "/" + maxSelectFileCount + ")");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterStorageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerStorageListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PATH, mPath);
    }

    @Override
    public void onBackStackChanged() {

        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
        } else {
            mPath = EXTERNAL_BASE_PATH;
        }

//        setTitle(mPath);
        if (HAS_ACTIONBAR)
            invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mFragmentManager.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the initial Fragment with given path.
     */
    private void addFragment() {
        FileListFragment fragment = FileListFragment.newInstance(mPath, maxSelectFileCount, selectFileType);
        mFragmentManager.beginTransaction().add(R.id.content, fragment).commit();
    }

    private void initActivityTitleUI() {
        mViewTitle = (TitleView) findViewById(R.id.title_filechooser);
        mBtnDone = (TextView) findViewById(R.id.tv_action);
        mViewTitle.setTitleText(getResources().getString(R.string.select_files));
        mViewTitle.setRightActionBtnText(getResources().getString(R.string.str_ok_nosp));
        mViewTitle.setBtnStyle(BtnStyle.RIGHT_ACTION);
    }

    public void onBackButtonClicked(View view) {
        if (null != callBack) {
            callBack.onLeftButtonClicked(view);
        }

        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
            mFragmentManager.popBackStack();
        } else {
            mPath = EXTERNAL_BASE_PATH;
            finish();
        }

//        setTitle(mPath);
        if (HAS_ACTIONBAR)
            invalidateOptionsMenu();
    }
    
    @Override
    public void setTitle(CharSequence title) {
    	super.setTitle(title);
    	if(null != mViewTitle){
    		mViewTitle.setTitleText(getTitle().toString());
    	}
    }
    
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (null == mViewTitle) {
            mViewTitle.setTitleText(getTitle().toString());
        }
    }

    public void onActionButtonClicked(View view) {
    	if(null != callBack){
    		callBack.onRightButtonClicked(view);
    	}
    }
    
    public void setTitleBarCallBack(ITitleBarCallBack callBack){
    	this.callBack = callBack;
    }
    
    public static boolean isHasActionbar() {
		return HAS_ACTIONBAR;
	}

	public FragmentManager getmFragmentManager() {
		return mFragmentManager;
	}

	/**
     * "Replace" the existing Fragment with a new one using given path. We're
     * really adding a Fragment to the back stack.
     * @param file The file (directory) to display.
     */
    private void replaceFragment(File file) {
        mPath = file.getAbsolutePath();

        FileListFragment fragment = FileListFragment.newInstance(mPath, maxSelectFileCount, selectFileType);
        mFragmentManager.beginTransaction().replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(mPath).commit();
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     * @param file The file selected.
     */
    private void finishWithResult(File file) {
        if (file != null) {
            Uri uri = Uri.fromFile(file);
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     * @param file The file selected.
     */
    private void finishWithResults(List<File> files) {
    	Intent intent = new Intent();
        intent.putExtra(PATHS, (Serializable) files);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the user selects a File
     * @param file The file that was selected
     */
    @Override
    public void onFileSelected(FileModel file) {
        if (file != null) {
            if (file.getmFile().isDirectory()) {
                replaceFragment(file.getmFile());
            } else {
                if(file.isSelected) {
                    finishWithResult(file.getmFile());
                } else {

                }
            }
        } else {
            Toast.makeText(FileChooserActivity.this, R.string.error_selecting_file, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mStorageListener, filter);
    }

    /**
     * Unregister the external storage BroadcastReceiver.
     */
    private void unregisterStorageListener() {
        unregisterReceiver(mStorageListener);
    }

    @Override
    public void onFileSelected(List<FileModel> files) {
        if (null != files && files.size() > 0) {
            ArrayList<File> fileLists = new ArrayList<File>();
            for (FileModel file2 : files) {
                if (file2.isSelected) {
                    fileLists.add(file2.getmFile());
                }
            }
            finishWithResults(fileLists);
        }
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //如果是选择附件则展示选择的附件个数；如果是替换附件，则选择附件的个数最多为一个
        if("select".equals(selectFileType)) {
            mBtnDone.setText(getResources().getString(R.string.str_down) + "(" + count + "/" + maxSelectFileCount + ")");
        } else {
            count--;
            mBtnDone.setText(getResources().getString(R.string.str_down) + "(" + count + "/" + maxSelectFileCount + ")");
        }
    }

}
