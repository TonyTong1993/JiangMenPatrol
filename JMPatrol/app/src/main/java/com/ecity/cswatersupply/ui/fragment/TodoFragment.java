package com.ecity.cswatersupply.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.eventcore.UIEvent;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.adapter.ArrayListAdapter;
import com.ecity.cswatersupply.adapter.TodoListAdapter;
import com.ecity.cswatersupply.adapter.TodoListAdapter.OnNotificationCheckStatusChangedListener;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.event.UIEventStatus;
import com.ecity.cswatersupply.model.NotificationSelection;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.project.ProjectModuleConfig;
import com.ecity.cswatersupply.project.adapter.ProjectNotificationAdapter;
import com.ecity.cswatersupply.project.service.ProjectService;
import com.ecity.cswatersupply.service.MessageService;
import com.ecity.cswatersupply.ui.activities.MainActivity;
import com.ecity.cswatersupply.ui.widght.CustomTitleView;
import com.ecity.cswatersupply.ui.widght.CustomTitleView.BtnStyle;
import com.ecity.cswatersupply.utils.ACache;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.LoadingDialogUtil;
import com.ecity.cswatersupply.utils.ToastUtil;
import com.ecity.cswatersupply.xg.model.Notification;
import com.ecity.cswatersupply.xg.util.NotificationAdapter;
import com.lee.pullrefresh.ui.PullToRefreshBase;
import com.lee.pullrefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.zzz.ecity.android.applibrary.dialog.AlertView;
import com.zzz.ecity.android.applibrary.dialog.AlertView.OnAlertViewListener;

public class TodoFragment extends ABaseListViewFragment<NotificationSelection> implements OnNotificationCheckStatusChangedListener {
    public static final String NOTIFICATION_ITEMS_ID = "items_id";
    private CheckBox checkBoxAll;
    private Button btnDelete;
    private View layoutDelete;
    private TextView tvMessageBlank;
    private int pageNo = 1;
    private static final int PAGE_SIZE = 15;
    private final List<String> selectedMessageIds = new ArrayList<String>();
    private int unreadCount = 0;
    private boolean isNotificationSelectionStatusChanged;
    private boolean isProjectNotification;
    private boolean isListNeedUpdate;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        initConfig();
        initUI(view);
        setListeners();
        EventBusUtil.register(this);
        if (pageNo == 1) { // 只在第一次加载的时候发请求。防止在tab间反复切换而反复调用。
            requestMessages();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            updateList();
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isListNeedUpdate) {
            isListNeedUpdate = false;
        } else {
            updateList();
        }
    }

    private void updateList(){
        updateTitle(unreadCount);
        groupSelectionsByStatus(getDataSource());
        if(null !=  getAdapter()) {
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected ArrayListAdapter<NotificationSelection> prepareListViewAdapter() {
        return new TodoListAdapter(getActivity(), this);
    }

    @Override
    protected List<NotificationSelection> prepareDataSource() {
        return getDataSource();
    }

    @Override
    protected int prepareLayoutResource() {
        return R.layout.fragment_todo_pulltorefresh_listview;
    }

    private void initConfig() {
        try {
            isProjectNotification = ProjectModuleConfig.getConfig().isModuleUseable();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(this, e);
        }
    }

    private void initUI(View view) {
        CustomTitleView titleView = getTitleView();
        titleView.setBtnStyle(BtnStyle.ONLY_RIGHT_ACTION);
        updateTitle(0);
        TextView tvAction = (TextView) titleView.findViewById(R.id.tv_action);
        tvAction.setText(R.string.todo_batch_delete);
        titleView.setMessageDeleteListener(new MessageDeleteListener());
        layoutDelete = view.findViewById(R.id.layout_delete);
        checkBoxAll = (CheckBox) view.findViewById(R.id.cb_select_all);
        btnDelete = (Button) view.findViewById(R.id.btn_delete);
        tvMessageBlank = (TextView) view.findViewById(R.id.tv_content_blank);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onDeleteButtonClicked();
            }
        });
    }

    private void setListeners() {
        checkBoxAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isNotificationSelectionStatusChanged) {
                    return;
                }
                updateAllSelectStatus(isChecked);
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    private void updateAllSelectStatus(boolean isSelected) {
        List<NotificationSelection> dataSource = getAdapter().getList();
        for (NotificationSelection item : dataSource) {
            item.setSelected(isSelected);
        }
    }

    /**
     * 在正常列表模式和批量删除模式间切换。
     */
    private void toggleMode() {
        TodoListAdapter adapter = (TodoListAdapter) getAdapter();
        adapter.setSelectEnabled(!adapter.isSelectEnabled());
        if (adapter.isSelectEnabled()) {
            selectedMessageIds.clear();
            getRefreshListView().onPullUpRefreshComplete();
        } else {
            updateAllSelectStatus(false);
            checkBoxAll.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        toggleDeleteSectionVisibility(adapter.isSelectEnabled());

        int actionButtonTitleId = adapter.isSelectEnabled() ? R.string.cancel : R.string.todo_batch_delete;
        getTitleView().setRightActionBtnText(actionButtonTitleId);
}

    private void toggleDeleteSectionVisibility(boolean isVisible) {
        int visibility = isVisible ? View.VISIBLE : View.GONE;
        layoutDelete.setVisibility(visibility);
    }

    @Override
    protected OnItemClickListener prepareOnItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationSelection item = getDataSource().get(position);
                if (isInSelectionMode()) {
                    item.setSelected(!item.isSelected());
                    onNotificationCheckStatusChanged(item);
                } else {
                    if (item.getNotification().isNew()) {
                        --unreadCount;
                        isListNeedUpdate = true;
                    }
                    item.getNotification().setStatus(Notification.NOTIFICATION_STATUS_PROCESSED);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(MainActivity.INTENT_KEY_NOTIFICATION, item.getNotification());
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected OnRefreshListener<ListView> prepareOnRefreshListener() {
        return new OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isInSelectionMode()) { // If we are deleting items, we do not allow refresh.
                    getRefreshListView().onPullUpRefreshComplete();
                    getRefreshListView().setLastUpdateTime();
                } else {
                    requestMessages();
                }
            }
        };
    }

    private boolean isInSelectionMode() {
        TodoListAdapter adapter = (TodoListAdapter) getAdapter();
        return adapter.isSelectEnabled();
    }

    private void requestMessages() {
        if (pageNo == 1) {
            LoadingDialogUtil.show(getActivity(), R.string.message_getting_messages);
        }
        User user = HostApplication.getApplication().getCurrentUser();
        //处理【江门外勤】 和【江门工程】的消息通知
        if (isProjectNotification) {
            ProjectService.getInstance().getMessages(user, pageNo, PAGE_SIZE, false);
        } else {
            MessageService.getInstance().getMessages(user, pageNo, PAGE_SIZE, false);
        }
    }

    /**
     * 修复Bug：将本地数据全部删除后，必须重新登录才能看到新的数据。
     */
    private void requestMessagesAfterDeletingMessages() {
        if (getDataSource().size() >= PAGE_SIZE) {
            return;
        }

        pageNo = 1;
        requestMessages();
    }

    public void onDeleteButtonClicked() {
        if (selectedMessageIds.size() == 0) {
            return;
        }

        String title = getString(R.string.dialog_title_prompt);
        String msg = getString(R.string.message_confirm_delete_messages);
        AlertView dialog = new AlertView(getActivity(), title, msg, new OnAlertViewListener() {
            @Override
            public void back(boolean result) {
                if (result) {
                    deleteMessages();
                }
            }
        }, AlertView.AlertStyle.YESNO);
        dialog.show();
    }

    protected boolean isPullRefreshEnabled() {
        return false;
    }

    @Override
    protected String getTitle() {
        return getActivity().getResources().getString(R.string.fragment_message_title);
    }

    private void deleteMessages() {
        LoadingDialogUtil.show(getActivity(), R.string.message_deleting_messages);
        MessageService.getInstance().deleteMessages(selectedMessageIds, checkBoxAll.isChecked());
    }

    public void onEventMainThread(ResponseEvent event) {
        if (!event.isOK()) {
            LoadingDialogUtil.dismiss();
            ToastUtil.showLong(event.getMessage());
            return;
        }

        switch (event.getId()) {
            case ResponseEventStatus.MESSAGE_QUERY:
                handleGetMessageResponse(event);
                break;
            case ResponseEventStatus.MESSAGE_DELETE:
                handleDeleteMessagesResponse(event);
            default:
                break;
        }
    }

    public void onEventMainThread(Notification notification) {
        unreadCount++;
        updateTitle(unreadCount);
        // 将新收到的通知显示到最上面。
        NotificationSelection selection = new NotificationSelection(notification, false);
        List<NotificationSelection> dataSource = getDataSource();
        dataSource.add(0, selection);
        updateDataSource(dataSource);
        tvMessageBlank.setVisibility(View.GONE);
        saveNotificationItemsId2Cache(notification.getNextStepIds());
    }

    private void handleGetMessageResponse(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        if (isInSelectionMode()) {
            return; // If user clicks "Delete" button before a request finishes, we ignore the request result.
        }

        JSONObject responseData = event.getData();
        JSONArray jsonArray = null;
        List<Notification> notifications  = null;
        if (isProjectNotification) {
            jsonArray = responseData.optJSONArray("features");
            notifications = ProjectNotificationAdapter.getInstance().adaptNotifications(jsonArray);
        } else {
            jsonArray = responseData.optJSONArray("msg");
            notifications = NotificationAdapter.instance.adaptNotifications(jsonArray);
        }
        if (ListUtil.isEmpty(notifications)) {
            UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST);
            uiEvent.setMessage(getString(R.string.message_no_more_record));
            uiEvent.setTargetClass(MainActivity.class);
            EventBusUtil.post(uiEvent);
            getRefreshListView().setHasMoreData(false);
        } else {
            List<NotificationSelection> selections = convertNotificationsToSelections(notifications);
            List<NotificationSelection> dataSource = getDataSource();
            if (pageNo == 1) {
                // 对于requestMessagesAfterDeletingMessages返回结果，要先把本地剩余的记录清空
                dataSource.clear();
            }
            dataSource.addAll(selections);
            groupSelectionsByStatus(dataSource);
            getRefreshListView().onPullUpRefreshComplete();
            getRefreshListView().setLastUpdateTime();
            updateDataSource(dataSource);
            pageNo++;
        }

        if (getDataSource().isEmpty()) {
            tvMessageBlank.setVisibility(View.VISIBLE);
        }
        
        if (isProjectNotification) {
            unreadCount = responseData.optInt("count");
        }else{
            unreadCount = responseData.optInt("unreadtotal");
        }
        updateTitle(unreadCount);
    }

    private void handleDeleteMessagesResponse(ResponseEvent event) {
        LoadingDialogUtil.dismiss();
        removeDeletedNotifications();

        UIEvent uiEvent = new UIEvent(UIEventStatus.TOAST);
        uiEvent.setMessage(getString(R.string.message_delete_success));
        uiEvent.setTargetClass(MainActivity.class);
        EventBusUtil.post(uiEvent);
        requestMessagesAfterDeletingMessages();
    }

    /*
     * 我的消息右上角的监听事件
     * 
     * */
    private class MessageDeleteListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            toggleMode();
        }
    }

    private void removeDeletedNotifications() {
        List<NotificationSelection> deletedNotifications = new ArrayList<NotificationSelection>();
        List<NotificationSelection> dataSource = getAdapter().getList();
        int deletedCount = 0;
        if (checkBoxAll.isChecked()) {
            unreadCount = deletedCount = 0;
            for (NotificationSelection item : dataSource) {
                deletedNotifications.add(item);
            }
        } else {
            for (NotificationSelection item : dataSource) {
                String messageId = String.valueOf(item.getNotification().getId());
                if (selectedMessageIds.contains(messageId)) {
                    deletedNotifications.add(item);
                    if (item.getNotification().isNew()) {
                        deletedCount++;
                    }
                }
            }
        }
        dataSource.removeAll(deletedNotifications);
        groupSelectionsByStatus(dataSource);
        getAdapter().notifyDataSetChanged();
        toggleMode();
        updateTitle(unreadCount -= deletedCount);
    }

    private List<NotificationSelection> convertNotificationsToSelections(List<Notification> notifications) {
        List<NotificationSelection> selections = new ArrayList<NotificationSelection>();
        for (Notification notification : notifications) {
            NotificationSelection selection = new NotificationSelection(notification, false);
            selections.add(selection);
        }

        return selections;
    }

    private void updateTitle(int unreadCount) {
        if (isAdded()) {
            String title = String.format(Locale.getDefault(), "%s(%d)", getString(R.string.message_center_title), unreadCount);
            getTitleView().setTitleText(title);
        }
    }

    private void groupSelectionsByStatus(List<NotificationSelection> selections) {
        List<NotificationSelection> newNotifications = new ArrayList<NotificationSelection>();
        List<NotificationSelection> processedNotifications = new ArrayList<NotificationSelection>();
        for (NotificationSelection selection : selections) {
            if (selection.getNotification().isNew()) {
                newNotifications.add(selection);
            } else {
                processedNotifications.add(selection);
            }
        }
        ListUtil.sort(newNotifications, false);
        ListUtil.sort(processedNotifications, false);
        selections.clear();
        selections.addAll(newNotifications);
        selections.addAll(processedNotifications);
    }

    @Override
    public void onNotificationCheckStatusChanged(NotificationSelection selection) {
        isNotificationSelectionStatusChanged = true;
        if (selection.isSelected()) {
            List<NotificationSelection> dataSource = getAdapter().getList();
            for (NotificationSelection item : dataSource) {
                String messageId = String.valueOf(item.getNotification().getId());
                if (item.isSelected()) {
                    if (!selectedMessageIds.contains(messageId)) {
                        selectedMessageIds.add(messageId);
                    }
                } else {
                    selectedMessageIds.remove(messageId);
                }
            }

            if (selectedMessageIds.size() == getDataSource().size()) {
                checkBoxAll.setChecked(true);
            }
        } else {
            String messageId = String.valueOf(selection.getNotification().getId());
            checkBoxAll.setChecked(false);
            selectedMessageIds.remove(messageId);
        }
        isNotificationSelectionStatusChanged = false;
    }

    private void saveNotificationItemsId2Cache(String nextStepId) {
        ACache aCache = ACache.get(mContext);
        aCache.put(NOTIFICATION_ITEMS_ID, nextStepId);
    }
}
