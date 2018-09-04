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

package com.ecity.medialibrary.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecity.medialibrary.R;
import com.ecity.medialibrary.activity.FileChooserActivity;
import com.ecity.medialibrary.activity.FileChooserActivity.ITitleBarCallBack;
import com.ecity.medialibrary.adapter.FileListAdapter;
import com.ecity.medialibrary.model.FileModel;
import com.ecity.medialibrary.utils.FileLoader;
import com.ecity.medialibrary.utils.MediaCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Fragment that displays a list of Files in a given path.
 * 
 * @version 2016-05-17
 * @author SunShan'ai
 */
public class FileListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<FileModel>> {

    /**
     * Interface to listen for events.
     */
    public interface Callbacks {
        /**
         * Called when a file is selected from the list.
         *
         * @param file The file selected
         */
        public void onFileSelected(FileModel file);

        public void onFileSelected(List<FileModel> files);

        void onSelectedCountChanged(int count);
    }

    private static final int LOADER_ID = 0;
    public static final String MAX_FILE_COUNT = "maxFileCount";
    public static final String SELECT_FILE_TYPE = "SELECT_FILE_TYPE";

    private FileChooserActivity mActivity;
    private FileListAdapter mAdapter;
    private String mPath;
    private boolean mListShown;
    private View mProgressContainer;
    private View mListContainer;
    private Callbacks mListener;
    private int maxSelectFileCount;
    private String fileSelectType;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), String.format(getResources().getString(R.string.str_select_attachment_limit), maxSelectFileCount), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * Create a new instance with the given file path.
     *
     * @param path The absolute path of the file (directory) to display.
     * @return A new Fragment with the given file path.
     */
    public static FileListFragment newInstance(String path, int maxFileCount, String selectType) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(FileChooserActivity.PATH, path);
        args.putInt(MAX_FILE_COUNT, maxFileCount);
        args.putString(SELECT_FILE_TYPE, selectType);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FileListFragment.Callbacks");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof FileChooserActivity) {
            mActivity = (FileChooserActivity) getActivity();
        }
        mAdapter = new FileListAdapter(getActivity(), mHandler);
        mAdapter.setTextCallback(mTextCallback);
        mPath = getArguments() != null ? getArguments().getString(FileChooserActivity.PATH) : Environment.getExternalStorageDirectory().getAbsolutePath();
        maxSelectFileCount = getArguments() != null ? getArguments().getInt(MAX_FILE_COUNT) : 1;
        fileSelectType = getArguments() != null ? getArguments().getString(SELECT_FILE_TYPE) : "select";
        mAdapter.setSelectTotal(MediaCacheManager.attachdrr.size());
        if("select".equals(fileSelectType)) {
            mAdapter.setMaxSelectFileCount(maxSelectFileCount);
        } else {
            mAdapter.setMaxSelectFileCount(2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_container, container, false);
        mListContainer = contentView.findViewById(R.id.listContainer);
        mProgressContainer = contentView.findViewById(R.id.progressContainer);
        mListShown = true;

        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setEmptyText(getString(R.string.empty_directory));
        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        setBaseActivityListener();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileListAdapter adapter = (FileListAdapter) l.getAdapter();
        if (adapter != null) {
            FileModel file = adapter.getItem(position);
            mPath = file.getmFile().getAbsolutePath();
            mListener.onFileSelected(file);
        }
    }

    @Override
    public Loader<List<FileModel>> onCreateLoader(int id, Bundle args) {
        return new FileLoader(getActivity(), mPath);
    }

    @Override
    public void onLoadFinished(Loader<List<FileModel>> loader, List<FileModel> data) {
        mAdapter.setListItems(data);

        if (isResumed())
            setListShown(true);
        else
            setListShownNoAnimation(true);
    }

    @Override
    public void onLoaderReset(Loader<List<FileModel>> loader) {
        mAdapter.clear();
    }

    @Override
    public void setEmptyText(CharSequence text) {
        TextView emptyView = (TextView) getListView().getEmptyView();
        emptyView.setVisibility(View.GONE);
        emptyView.setText(text);
    }

    public void setListShown(boolean shown, boolean animate) {
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.INVISIBLE);
        }
    }

    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    private void setBaseActivityListener() {
        if (null == mActivity) {
            return;
        }

        mActivity.setTitleBarCallBack(new ITitleBarCallBack() {

            @Override
            public void onRightButtonClicked(View view) {
                mListener.onFileSelected(mAdapter.getListItems());
                updateAttachAdrr();
            }

            @Override
            public void onLeftButtonClicked(View view) {
            }
        });
    }

    private FileListAdapter.TextCallback mTextCallback = new FileListAdapter.TextCallback() {
        @Override
        public void onListen(int count) {
            mListener.onSelectedCountChanged(count);
        }
    };

    private void updateAttachAdrr() {
        ArrayList<String> list = new ArrayList<String>();
        Collection<String> c = mAdapter.map.values();
        Iterator<String> it = c.iterator();
        for (; it.hasNext(); ) {
            list.add(it.next());
        }
        if (MediaCacheManager.act_bool) {
            MediaCacheManager.act_bool = false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (MediaCacheManager.attachdrr.size() < maxSelectFileCount) {
                MediaCacheManager.attachdrr.add(list.get(i));
            }
        }
    }
}
