package com.ecity.cswatersupply.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.android.log.LogUtil;
import com.ecity.cswatersupply.adapter.checkitem.InspectItemAdapter;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.checkitem.InspectItem;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.PunishmentDetailsParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.network.response.eventreport.EventReportResponse;

/**
 * 获取罚单详情信息
 * 
 * @author lotus
 * 
 */
public class PunishmentDetailService {
    private static PunishmentDetailService instance;
    static {
        instance = new PunishmentDetailService();
    }

    private PunishmentDetailService() {

    }

    public static PunishmentDetailService getInstance() {
        return instance;
    }

    public void getPunishments(final String url, final String eventId, final String eventType) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new PunishmentDetailsParameter(eventId, eventType);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                EventReportResponse response = null;
                try {
                    response = new EventReportResponse();
                    String reString = jsonObj.toString().replace("GEOM", "TXT").replace("DDL", "TXT").replace("DATE", "TXT");
                    JSONObject newObject = new JSONObject(reString);
                    JSONArray items = newObject.optJSONArray("params").optJSONObject(0).getJSONArray("items");
                    List<InspectItem> adaptItems = InspectItemAdapter.adaptItems(items);
                    response.setInspectItems(adaptItems);
                } catch (JSONException e) {
                    LogUtil.e(this, e);
                }
                return response;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.PUNISHMENT_DETAILS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }
}
