package com.ecity.cswatersupply.xg.model;

/**
 * 
 * @author gaokai
 *
 */
public enum NotificationType {
    plan,
    workOrder,  //用于更新主界面“管网维修”的通知数量
    wo_new,
    wo_need_receive,
    wo_received,
    wo_explored,
    wo_signed,
    wo_finish_applied,
    wo_finish_rejected,
    wo_finish_approved,
    wo_progress_updated,
    wo_delay_applied,
    wo_delay_approved,
    wo_delay_rejected,
    wo_delay_cancelled,
    wo_return_applied,
    wo_return_approved,
    wo_return_rejected,
    wo_transfer_applied,
    wo_tranfer_approved,
    wo_transfer_rejected,
    wo_assist_applied,
    wo_assist_approved,
    wo_assist_rejected,
    wo_notify_count,
    wo_backtransfer_applied,
    wo_backtransfer_rejected,
    wo_backtranfer_approved,
    wo_back_platform,
    wo_backworkorder_rejected,
    xj_badge_view_key, //用于更新主界面“巡检任务”的通知数量
    xj_task,
    xj_point_arrived,
    xj_zhuan_task,
    yh_badge_view_key, //用于更新主界面“养护任务”的通知数量
    yh_task,
    
    //=====start  -- Sign ======
    zb_squeezed_out,
    zb_time_out,
    //=====end  -- Sign =========

    //=====event  -- Sign ======
    sj_report_event,
    sj_eventtoworkorder,
    sj_close,
    sj_eventtotask,
    //=====event  -- Sign =========

    yh_point_arrived,
    //=====start  -- News announcement ======
    wuhandizhen_earthquake_msg,
    wuhandizhen_announcement_msg,
    wuhandizhen_expertopinion_msg,
    
    //=====end  -- News announcement =========

    //project start
    kg_check_pass,
    kg_check_unpass,
    kg_apply,
    sy_check_pass,
    sy_check_unpass,
    sy_make,
    js_gcb_check_pass,
    js_gcb_check_unpass,
    js_gwb_check_pass,
    js_gwb_check_unpass,
    js_apply,
    workload_apply,
    workload_check,
    pay_apply,
    pay_check,
    jg_check_pass,
    jg_check_unpass,
    jg_apply,
    sj_delegate,
    sj_back,
    sj_delay_apply,
    sj_delay_check,
    sj_upload,
    sj_design_check,
    //project end

    //czdz begin
	zqsb
    //czdz end
}
