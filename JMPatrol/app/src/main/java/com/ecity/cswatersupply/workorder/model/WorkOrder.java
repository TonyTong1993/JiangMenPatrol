package com.ecity.cswatersupply.workorder.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.ecity.cswatersupply.model.AModel;
import com.ecity.cswatersupply.utils.GeometryUtil;
import com.ecity.cswatersupply.utils.PlanTaskUtils;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

public class WorkOrder extends AModel {
    /**
     * 以下字段，用于从工单attributes中取值，要跟服务同步
     */
    public static final String KEY_DISPATCHER = "assign_userid"; // 派单人
    public static final String KEY_COMLETE_TIME = "reportfinish_deal_time";// 完结时间
    public static final String KEY_DEADLINE_TIME = "NULL";// 延期截止时间
    public static final String KEY_FANYING_REGION = "reflect_block";// 反映块号
    public static final String KEY_DISPATCH_TIME = "assign_time";// 派单时间
    public static final String KEY_CONTENT = "base_fy_content";// 工单内容
    public static final String KEY_CODE = "base_workorderid";// 工单编码
    public static final String KEY_STATE = "base_state";// 工单状态
    public static final String KEY_SUB_STATE = "base_substate";// 工单分支状态
    public static final String KEY_DISPLAY_STATE = "display_state";// 用于显示的状态
    public static final String KEY_ADDRESS = "base_fs_address";// 地址
    public static final String KEY_REMARK = "base_remark";// 备注信息 
    public static final String KEY_ASKFINISH_TIME = "base_askfinishtime";// 要求完成时间
    public static final String KEY_PHONE = "base_phone";// 联系电话
    public static final String KEY_FIXTIME = "fixtime";// 约期
    public static final String KEY_FROM = "base_source";// 工单来源
    public static final String KEY_FROM_ALIAS = "base_source_alias";// 工单来源
    public static final String KEY_ID = "processinstanceid";
    public static final String KEY_MAIN_MAN = "assign_main_workman"; // 主办人
    public static final String KEY_ASSIST_MAN = "assign_assistances";
    public static final String KEY_CAN_OPERATOR = "CANOPERATOR"; // 同一单子只能主办人操作，协办人只能看
    public static final String KEY_OPERATE_BTNS = "operationbuttons"; // 工单按钮
    public static final String KEY_TRANSFER_ACCEPTER = "transfer_receiver"; //转办接收人
    public static final String KEY_DELAY_CHECK_OPINION = "delay_opinion"; // 延期审核意见
    public static final String KEY_TRANSFER_CHECK_OPINION = "receiver_remark"; // 转办审核意见
    public static final String KEY_ASSIST_CHECK_OPINION = "assist_audit_remark"; // 协助审核意见  //assist_remark
    public static final String KEY_RETURN_CHECK_OPINION = "back_audit_remark"; // 退单审核意见 back_remark
    public static final String KEY_TOTAL_TIME = "totaltime"; // 结单维修工时
    public static final String KEY_REPORTFINISH_DEAL_TYPE = "reportfinish_deal_type"; // 完工上报的处理类别
    public static final String KEY_REPORTFINISH_DEAL_BGFKJ = "reportfinish_deal_bgfkj"; // 完工上报的表管阀口径
    public static final String KEY_REPORTFINISH_DEAL_BGFFL = "reportfinish_deal_bgffl"; // 完工上报的表管阀分类
    public static final String KEY_PUMP_NAME = "pump_name"; // 完工上报的泵房名称
    public static final String INTENT_KEY_CLICKED_BUTTON = "clicked_button"; // 点击的按钮
    
    
    /**
     * 用于序列化
     */
    public static final String KEY_SERIAL = "workOrder";

    private static final long serialVersionUID = 1L;

    private String geomString = "";
    private Map<String, String> attributes = new LinkedHashMap<String, String>();// 保留元数据顺序

    private List<WorkOrderBtnModel> workOrderBtns;

    public List<WorkOrderBtnModel> getWorkOrderBtns() {
        return workOrderBtns;
    }

    public void setWorkOrderBtns(List<WorkOrderBtnModel> workOrderBtns) {
        this.workOrderBtns = workOrderBtns;
    }

    public Map<String, String> currentList() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public String getGeomString() {
        return geomString;
    }

    public void setGeomString(String geomString) {
        this.geomString = geomString;
    }

    public String getAttribute(String key) {
        String value = attributes.get(key);
        return (value == null) ? "" : value;
    }

    public Point getPoint() {
        Point point = new Point(0, 0);
        try {
            if (TextUtils.isEmpty(geomString)) {
                return point;
            } else {
                Geometry geometry = PlanTaskUtils.buildGeometryFromJSON(geomString);
                if (null == geometry) {
                    return point;
                }
                return GeometryUtil.GetGeometryCenter(geometry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return point;
        }
    }

    public boolean hasLocationInfo() {
        if (TextUtils.isEmpty(geomString)) {
            return false;
        } else {
            Geometry geometry = PlanTaskUtils.buildGeometryFromJSON(geomString);
            if (null == geometry) {
                return false;
            }
            Point point = GeometryUtil.GetGeometryCenter(geometry);
            return point.getX() > 0.0 && point.getY() > 0.0;
        }
    }

    @Override
    // 工单ID和工单状态一起用来区分工单
    public boolean equals(Object other) {
        if (this == other) // 先检查是否其自反性，后比较other是否为空。这样效率高
            return true;
        if (other == null)
            return false;
        if (!(other instanceof WorkOrder))
            return false;

        final WorkOrder otherWorkOrder = (WorkOrder) other;

        String thisCode = attributes.get(KEY_ID);
        String otherCode = otherWorkOrder.getAttributes().get(KEY_ID);
        if (thisCode == null || otherCode == null) {
            return false;
        }
        if (!thisCode.equals(otherCode)) {
            return false;
        }
        String state = attributes.get(KEY_STATE);
        String otherState = otherWorkOrder.getAttributes().get(KEY_STATE);
        if (!state.equals(otherState)) {
            return false;
        }
        String subState = attributes.get(KEY_SUB_STATE);
        String otherSubState = otherWorkOrder.getAttributes().get(KEY_SUB_STATE);
        return subState.equals(otherSubState);
    }

    @Override
    public int hashCode() {
        int result = 17;
        String thisCode = attributes.get(KEY_ID);
        String state = attributes.get(KEY_STATE);
        String subState = attributes.get(KEY_SUB_STATE);

        result = 37 * result + thisCode.hashCode();
        result = 37 * result + state.hashCode();
        result = 37 * result + subState.hashCode();
        return result;
    }
}
