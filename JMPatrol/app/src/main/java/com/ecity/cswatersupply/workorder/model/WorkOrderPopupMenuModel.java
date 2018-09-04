package com.ecity.cswatersupply.workorder.model;

import com.ecity.cswatersupply.Constants;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.utils.ResourceUtil;
import com.z3app.android.util.StringUtil;

/**
 * 工单工单列表弹窗配置项
 *
 * @author qiwei
 */
public enum WorkOrderPopupMenuModel {
    /*============================================待处理==========================================================**/
    /**
     * 待处理全部
     */
    WORKORDER_TODO_ALL(ResourceUtil.getStringById(R.string.menu_workorder_todo_all), 0, Constants.WAIT_PROCESS, Constants.WAIT_PROCESS_ORDER), /**
     * 待分派
     */
    WORKORDER_TODO_MY_DISPATCH(ResourceUtil.getStringById(R.string.menu_workorder_todo_my_wait_dispatch), 0, "mywaitdispath", Constants.WAIT_PROCESS_ORDER), /**
     * 待接单
     */
    WORKORDER_TODO_MY_RECIEVE(ResourceUtil.getStringById(R.string.menu_workorder_todo_my_wait_recieve), 0, "mywaitreceive", Constants.WAIT_PROCESS_ORDER), /**
     * 待审核
     */
    WORKORDER_TODO_MY_CHECK(ResourceUtil.getStringById(R.string.menu_workorder_todo_my_wait_check), 0, "mywaitcheck", Constants.WAIT_PROCESS_ORDER),

    /**
     * 本班组
     */
    WORKORDER_TODO_MY_TEAM(ResourceUtil.getStringById(R.string.menu_workorder_todo_my_team), 0, "myTeam", Constants.WAIT_PROCESS_ORDER), /**
     * 非本班组
     */
    WORKORDER_TODO_NOT_MY_TEAM(ResourceUtil.getStringById(R.string.menu_workorder_todo_not_my_team), 0, "notMyTeam", Constants.WAIT_PROCESS_ORDER),
    /*================================================处理中===========================================================**/
    /**
     * 处理中全部
     */
    WORKORDER_OPERATOR_ALL(ResourceUtil.getStringById(R.string.menu_workorder_process_all), 1, Constants.EXCUTE_PEOCESS, Constants.EXCUTE_PEOCESS_ORDER), /**
     * 正在处理中
     */
    WORKORDER_OPERATOR_MY_EXCUTE(ResourceUtil.getStringById(R.string.menu_workorder_process_my_excute), 1, "myexecute", Constants.EXCUTE_PEOCESS_ORDER), /**
     * 正在协助中
     */
    WORKORDER_OPERATOR_MY_ASSIGN(ResourceUtil.getStringById(R.string.menu_workorder_process_my_assisgn), 1, "myassist", Constants.EXCUTE_PEOCESS_ORDER), /**
     * 班组中待接单
     */
    WORKORDER_OPERATOR_TEAM_RECIEVE(ResourceUtil.getStringById(R.string.menu_workorder_process_wait_team_reciever), 1, "teamwaitreceive", Constants.EXCUTE_PEOCESS_ORDER), /**
     * 班组中待处理
     */
    WORKORDER_OPERATOR_TEAM_EXCUTE(ResourceUtil.getStringById(R.string.menu_workorder_process_wait_team_excute), 1, "teamexecute", Constants.EXCUTE_PEOCESS_ORDER),
    /*================================================完工==========================================================**/
    /**
     * 完工全部
     */
    WORKORDER_FINSH_ALL(ResourceUtil.getStringById(R.string.menu_workorder_finish_all), 2, Constants.FINISH_PROCESS, Constants.FINISH_PROCESS_ORDER), /**
     * 完工
     */
    WORKORDER_FINSH_MY_FINISH(ResourceUtil.getStringById(R.string.menu_workorder_finish_my_finish), 2, "myfinish", Constants.FINISH_PROCESS_ORDER), /**
     * 退单完工
     */
    WORKORDER_FINSH_BACK_FINISH(ResourceUtil.getStringById(R.string.menu_workorder_finish_back_finish), 2, "backfinish", Constants.FINISH_PROCESS_ORDER), /**
     * 转单完工
     */
    WORKORDER_FINSH_TRANSFER_FINISH(ResourceUtil.getStringById(R.string.menu_workorder_finish_transfer_finish), 2, "transferfinish", Constants.FINISH_PROCESS_ORDER);

    private String name;
    private int tag;
    private String tagName;
    private String orderBy;

    /**
     * @param name 用于显示
     */
    WorkOrderPopupMenuModel(String name, int tag, String tagName, String orderBy) {
        this.name = name;
        this.tag = tag;
        this.tagName = tagName;
        this.orderBy = orderBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getName() {
        return this.name;
    }

    public String getTagName() {
        return this.tagName;
    }

    public int getTag() {
        return this.tag;
    }

    public static WorkOrderPopupMenuModel getPageByName(String name, int tag) {
        for (WorkOrderPopupMenuModel p : values()) {
            if (p.getName().equals(name) && p.getTag() == tag) {
                return p;
            }
        }
        return null;
    }
}
