package com.ecity.cswatersupply.workorder.view;

import java.util.List;

import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.cswatersupply.workorder.model.WorkOrder;
import com.ecity.cswatersupply.workorder.presenter.BasePresenter;

/**
 * 工单契约接口 作用:可以一览所有接口
 * 
 * @author gaokai
 *
 */
public interface WorkOrderContract {

    /**
     * MVP之V，负责更新UI操作
     */
    interface View extends BaseView<Presenter> {

        /**
         * 展示状态条
         */
        void showStatusBar(CharSequence msg);

        void showStatusBar(int msgResId);

        /**
         * 显示加载完毕后的状态
         */
        void showCompletionStatus();

        /**
         * 置顶显示
         * 
         * @param workOrders
         */
        void showTop(List<List<WorkOrder>> workOrders);

        /**
         * 更新列表
         */
        void updateList(List<List<WorkOrder>> list);

        /**
         * 显示加载对话框
         */
        void showLoadingDialog(int msgResId);
    }

    /**
     * MVP之P，负责业务逻辑或者更新UI的逻辑
     */
    interface Presenter extends BasePresenter {
        /**
         * 加载新数据
         * 
         * @param showNotification
         *            成功后是否显示通知
         */
        void onLoadingNewData(boolean showNotification);

        /**
         * 加载更多数据（暂时未用）
         */
        void onLoadingMoreData();

        /**
         * 更新UI逻辑
         */
        void updateUI(int messageResId, Object obj);

        /**
         * 请求接单
         */
        void applyAcceptWorkOrder(String gid);

        /**
         * 处理下载完的工单
         */
        void handleDownloadedWorkOrder(int messageResId, ResponseEvent event);

        void setGroupPosition(int position);

        String getCurrentGid();
    }
}
