package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.R;

/**
 * 工单处置，一级界面元数据
 * 
 * @author gaokai
 *
 */
public enum WorkOrderGroupEnum {
    /**
     * 待处理工单：待分派、待接单、退单通过
     */
    NOHANDLE(R.drawable.workorder_icon_waitting, R.color.darkorange, R.string.workorder_group_no_handle, 0, false, "2,3,24,29", "22,23,25"),
    /**
     * 处理中工单：待勘察、待上门维修、待完工、延期通过、延期驳回、协助通过、协助驳回、完工驳回、延期申请、协助申请、退单申请、转办通过、转办驳回、退单驳回、延期取消、协助取消、转办取消、退回取消
     */
    HANDLLING(R.drawable.workorder_icon_processing, R.color.blue_normal, R.string.workorder_group_handlling, 0, false, "4,5,6,14,15,17,18,20,22,23,25,27,28,30,31,32,33,34", "24,29"),
    /**
     * 已完成工单：完工审核
     */
    COMLETE(R.drawable.workorder_icon_history, R.color.green, R.string.workorder_group_complete, 0, false, "7,21,35,36,37", "");

    /**
     * 图标资源ID
     */
    private int iconResId;
    /**
     * 标题资源ID
     */
    private int titleResId;
    /**
     * 已经获得的工单数量
     */
    private int initialCount;
    /**
     * 是否有新工单
     */
    private boolean hasNew;
    /**
     * tips颜色
     */
    private int colorResId;
    /**
     * 该组装载这些状态下的工单{@WorkOrderState}
     */
    private String containState;
    /**
     * 组长专用
     */
    private String leaderState;

    /**
     * @param state 组长和组员都适用
     * @param leadrState 组长专用。例如：延期申请单子，在组员手里是装在处理中分组，在组长手里装在待处理分组
     */
    WorkOrderGroupEnum(int iconResId, int colorResId, int titleResId, int initialCount, boolean hasNew, String state, String leaderState) {
        this.iconResId = iconResId;
        this.titleResId = titleResId;
        this.initialCount = initialCount;
        this.colorResId = colorResId;
        this.hasNew = hasNew;
        this.containState = state;
        this.leaderState = leaderState;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getTitle() {
        return titleResId;
    }

    public void setTitle(int titleResId) {
        this.titleResId = titleResId;
    }

    public int getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(int initialCount) {
        this.initialCount = initialCount;
    }

    public boolean hasNew() {
        return hasNew;
    }

    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }

    public int getColorResId() {
        return colorResId;
    }

    public void setColorResId(int colorResId) {
        this.colorResId = colorResId;
    }

    public String getContainState() {
        return containState;
    }

    public void setContainState(String containState) {
        this.containState = containState;
    }

    public String getLeaderState() {
        return leaderState;
    }

    public void setLeaderState(String leaderState) {
        this.leaderState = leaderState;
    }
}
