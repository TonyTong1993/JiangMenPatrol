package com.ecity.cswatersupply.project.network.request;

import com.ecity.android.db.utils.StringUtil;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

import java.util.HashMap;
import java.util.Map;

//* @param code 项目那编码
//        * @param name 项目名称
//        * @param type 项目类型
//        * @param kgtime0 开工时间开始
//        * @param kgtime1 开工时间结束
//        * @param orderField 排序字段
//        * @param pageNo 页码
//        * @param pageSize 页大小
//        * @param userid 当前用户ID

public class GetSafeProjectListParameter implements IRequestParameter {
    private User user;
    private int pageNo;
    private int pageSize;
    private SearchProjectParameter searchProjectParameter;

    public GetSafeProjectListParameter(User user, int pageNo, int pageSize, SearchProjectParameter searchProjectParameter) {
        this.user = user;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.searchProjectParameter = searchProjectParameter;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", user.getId());
        map.put("pageno", String.valueOf(pageNo));
        map.put("pagesize", String.valueOf(pageSize));
        if (null != searchProjectParameter) {
            String proName = searchProjectParameter.getProName();
            if (!StringUtil.isEmpty(proName)) {
                map.put("name", proName);
            }
            String proCode = searchProjectParameter.getProCode();
            if (!StringUtil.isEmpty(proCode)) {
                map.put("code", proCode);
            }
            int proTypeid = searchProjectParameter.getProTypeid();
            if (proTypeid > 0) {
                map.put("type", String.valueOf(proTypeid));
            }
            String startTime = searchProjectParameter.getStartTime();
            if (!StringUtil.isEmpty(startTime)) {
                map.put("kgtime0", startTime);
            }
            String endTime = searchProjectParameter.getEndTime();
            if (!StringUtil.isEmpty(endTime)) {
                map.put("kgtime1", endTime);
            }
        }
        return map;
    }

}
