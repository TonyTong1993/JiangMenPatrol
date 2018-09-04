package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.utils.ListUtil;

public class DeleteMessageParameter implements IRequestParameter {
    private List<String> messageIds;
    private boolean isDeleteAll;

    public DeleteMessageParameter(List<String> messageIds, boolean isDeleteAll) {
        this.messageIds = messageIds;
        this.isDeleteAll = isDeleteAll;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (isDeleteAll) {
            map.put("userid", getUserID());
        } else {
            map.put("gids", concatenateMessageIds());
        }
        return map;
    }

    private String concatenateMessageIds() {
        if (ListUtil.isEmpty(messageIds)) {
            return "";
        }

        StringBuilder idBuilder = new StringBuilder("");
        for (String messageId : messageIds) {
            if (idBuilder.length() > 0) {
                idBuilder.append(",");
            }
            idBuilder.append(messageId);
        }

        return idBuilder.toString();
    }

    private String getUserID() {
        return HostApplication.getApplication().getCurrentUser().getId();
    }
}
