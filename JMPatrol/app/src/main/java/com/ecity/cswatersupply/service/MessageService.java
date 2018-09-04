package com.ecity.cswatersupply.service;

import java.util.List;

import org.json.JSONObject;

import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.DeleteMessageParameter;
import com.ecity.cswatersupply.network.request.GetMessageParameter;
import com.ecity.cswatersupply.network.request.ReadMessageParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;

public class MessageService {
    private static MessageService instance;

    static {
        instance = new MessageService();
    }

    private MessageService() {
    }

    public static MessageService getInstance() {
        return instance;
    }

    public void getMessages(final User user, final int pageNo, final int pageSize, final boolean isProcessed) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getQueryMessageUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetMessageParameter(user, pageNo, pageSize, isProcessed);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MESSAGE_QUERY;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void readMessage(final int messageId) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getReadMessageUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new ReadMessageParameter(messageId);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MESSAGE_READ;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    public void deleteMessages(final List<String> messageIds, final boolean isDeleteAll) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.POST;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getDeleteMessageUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new DeleteMessageParameter(messageIds, isDeleteAll);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.MESSAGE_DELETE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

}
