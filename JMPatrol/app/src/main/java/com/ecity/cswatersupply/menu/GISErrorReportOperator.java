package com.ecity.cswatersupply.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.httpext.RequestParams;
import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.EInspectItemType;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.FileUploader;
import com.ecity.cswatersupply.network.FileUploader2;
import com.ecity.cswatersupply.network.IFileUploaderReponseParser;
import com.ecity.cswatersupply.network.request.AReportInspectItemParameter;
import com.ecity.cswatersupply.network.request.GisErrorReportParameter;
import com.ecity.cswatersupply.service.ServiceUrlManager;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

public class GISErrorReportOperator extends EventReportOperator1 {
    private String objectId;
    private List<InspectItem> inspectItems;

    @Override
    protected String getServiceUrl() {
        return ServiceUrlManager.getInstance().submitGisErrorUrl(String.valueOf(SessionManager.GISReportLayerID));
    }

    @Override
    protected String getReportImageUrl() {
        return ServiceUrlManager.getInstance().getUploadAttachmentsUrl(SessionManager.GISReportTableName, objectId);
    }

    @Override
    protected AReportInspectItemParameter getRequestParameter() {
        return new GisErrorReportParameter(SessionManager.currentInspectItems);
    }

    @Override
    protected int getRequestId() {
        return ResponseEventStatus.EVENT_REPORT_GIS_ERROR_REPORT;
    }

    @Override
    public void submit2Server(List<InspectItem> mInspectItems) {
        super.submit2Server(mInspectItems);
    }

    @Override
    public List<InspectItem> getDataSource() {
        inspectItems = new ArrayList<InspectItem>();
        Context context = HostApplication.getApplication();
        addInspectItems(true, EInspectItemType.GEOMETRY_AREA, context.getString(R.string.gis_error_location), "", "");
        addInspectItems(true, EInspectItemType.DROPDOWNLIST, context.getString(R.string.gis_error_type), "", getGisErrorTypeSelectValues().toString());
        addInspectItems(false, EInspectItemType.TEXT, context.getString(R.string.gis_error_statement), "", "");
        addInspectItems(true, EInspectItemType.IMAGE, context.getString(R.string.photos), "", "");

        return inspectItems;
    }

    @Override
    protected void uploadForm(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        FileUploader.execute(filePaths, url, parameters, handler, new IFileUploaderReponseParser() {

            @Override
            public boolean isSuccess(String responseResult) {
                JSONObject jsonObj = JsonUtil.getJsonObject(responseResult);
                JSONArray jsonArray = jsonObj.optJSONArray("addResults");
                if ((jsonArray == null) || (jsonArray.length() == 0)) {
                    return false;
                }

                jsonObj = jsonArray.optJSONObject(0);
                if (!jsonObj.optBoolean("success")) {
                    return false;
                }

                objectId = jsonObj.optString("objectId");
                return true;
            }
        });
    }

    @Override
    protected void uploadFiles(List<String> filePaths, String url, Map<String, String> parameters, Handler handler) {
        EventBusUtil.register(this);
        RequestParams requestParams = new RequestParams();
        requestParams.put("remark", "");
        requestParams.put("type", "");
        requestParams.put("filename", "");
        requestParams.put("dbtype", "2");
        requestParams.put("f", "json");
        FileUploader2.execute(filePaths, url, requestParams);
    }

    public void onEventMainThread(ResponseEvent event) {
        switch (event.getId()) {
            case ResponseEventStatus.UPLOAD_SINGLE_FILE:
                processUploadSingleFileResponse(event);
                EventBusUtil.unregister(this);
                break;
            case ResponseEventStatus.UPLOAD_ALL_FILES:
                processUploadAllFilesResponse(event);
                EventBusUtil.unregister(this);
                break;
            default:
                break;
        }
    }

    private void processUploadSingleFileResponse(ResponseEvent event) {
        Message msg = Message.obtain();
        if (event.isOK()) {
            msg.what = FileUploader.UPLOAD_SINGLE_FILE_DONE;
            msg.obj = event.getData();
        } else {
            msg.what = FileUploader.UPLOAD_FAIL;
        }
        Handler handler = getEventHandler();
        handler.sendMessage(msg);
    }

    private void processUploadAllFilesResponse(ResponseEvent event) {
        Message msg = Message.obtain();
        if (event.isOK()) {
            msg.what = FileUploader.UPLOAD_ALL_FILES_DONE;
            msg.obj = HostApplication.getApplication().getString(R.string.upload_success);
        } else {
            msg.what = FileUploader.UPLOAD_FAIL;
        }
        Handler handler = getEventHandler();
        handler.sendMessage(msg);
    }

    private void addInspectItems(boolean required, EInspectItemType type, String name, String value, String selectValues) {
        InspectItem statementItem = new InspectItem(true, required, type, name, name, value, "", selectValues);
        statementItem.setEdit(true);
        inspectItems.add(statementItem);
    }

    private static JSONArray getGisErrorTypeSelectValues() {
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject josn1 = new JSONObject();
            Object type1 = ResourceUtil.getStringById(R.string.gis_error_geometry);
            josn1.put("alias", type1);
            josn1.put("name", type1);
            jsonarray.put(josn1);

            JSONObject josn2 = new JSONObject();
            Object type2 = ResourceUtil.getStringById(R.string.gis_error_property);
            josn2.put("alias", type2);
            josn2.put("name", type2);
            jsonarray.put(josn2);

            return jsonarray;
        } catch (Exception e) {
        }

        return null;
    }

}
