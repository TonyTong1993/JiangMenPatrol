package com.ecity.cswatersupply.service;

import     com.ecity.android.eventcore.EventBusUtil;
import com.ecity.android.eventcore.ResponseEvent;
import com.ecity.android.httpforandroid.http.HttpRequestType;
import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.event.ResponseEventStatus;
import com.ecity.cswatersupply.menu.AppMenu;
import com.ecity.cswatersupply.model.User;
import com.ecity.cswatersupply.network.ARequestCallback;
import com.ecity.cswatersupply.network.RequestExecutor;
import com.ecity.cswatersupply.network.RequestParameter;
import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;
import com.ecity.cswatersupply.network.request.ChangePasswordParameter;
import com.ecity.cswatersupply.network.request.GetAllPatrolManParameter;
import com.ecity.cswatersupply.network.request.GetPatrolBusParameter;
import com.ecity.cswatersupply.network.request.GetPatrolManTrackParameter;
import com.ecity.cswatersupply.network.request.GetPatrolTreeParameter;
import com.ecity.cswatersupply.network.request.LoginParameter;
import com.ecity.cswatersupply.network.request.LoginTokenParameter;
import com.ecity.cswatersupply.network.request.SignInParameter;
import com.ecity.cswatersupply.network.request.SignOutParameter;
import com.ecity.cswatersupply.network.request.WatchStateParameter;
import com.ecity.cswatersupply.network.response.AServerResponse;
import com.ecity.cswatersupply.network.response.loginresponse.LoginResponse;
import com.ecity.cswatersupply.network.response.loginresponse.LoginTokenResponse;
import com.ecity.cswatersupply.utils.JsonUtil;
import com.ecity.cswatersupply.utils.ListUtil;
import com.ecity.cswatersupply.utils.ResourceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private volatile static UserService instance;
    private List<AppMenu> availableMenus = new ArrayList<AppMenu>();
    private List<AppMenu> availableRightMenus = new ArrayList<AppMenu>();
    private List<AppMenu> dynamicTabMenus = new ArrayList<AppMenu>();

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public void login(final String account, final String password) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getLoginUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {

                return new LoginTokenParameter(account, password);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.LoginGetTokenEvent;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.paraseJSONTOLoginTokenResponse(jsonObj);
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }


        });
    }

    public void getUserInfoByToken(final String token) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getUserInfoByToken();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new LoginParameter(token);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.LoginEvent;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {

                return AServerResponse.class;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.paraseJSONObjectToLoginResponse(jsonObj);
            }
        });
    }

    public List<AppMenu> getAvailableMenus() {
        return availableMenus;
    }

    public void getIsWatchState() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getHasWatchStateUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return null;
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WatchState;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWatchStateResult(jsonObj);
            }
        });
    }

    /**
     * 获取人员值班签到状态
     *
     * @param user
     */
    public void getWatchState(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getWatchStateUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new WatchStateParameter(user);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.WatchEvent;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseWatchResult(jsonObj);
            }
        });
    }

    /**
     * Set names of menus that are available to current user.
     *
     * @param menus
     */
    public void setAvailableMenus(ArrayList<AppMenu> menus) {
        availableMenus = menus;
    }

    /***
     * 获得江门工程的主菜单
     * @return
     */
    public List<AppMenu> getAvailableRightMenus() {
        return availableRightMenus;
    }

    public void setAvailableRightMenus(List<AppMenu> availableRightMenus) {
        if (ListUtil.isEmpty(availableRightMenus)) {
            return;
        }
        this.availableRightMenus = availableRightMenus;
    }

    public List<AppMenu> getDynamicTabMenus() {
        return dynamicTabMenus;
    }

    public void setDynamicTabMenus(List<AppMenu> dynamicTabMenus) {
        this.dynamicTabMenus = dynamicTabMenus;
    }

    public void getMobileConfigFile(final String url) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            public boolean isForComplexResponse() {
                return false;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new EmptyRequestParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_MAPCONFIG_EVENT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_mapconfig_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }
        });
    }

    public void changePassword(final String json, final User user, final String oldPassword, final String newPassword) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            @Override
            public IRequestParameter prepareParameters() {
                return new ChangePasswordParameter(json, user, oldPassword, newPassword);
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getChangePasswordUrl();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.USER_CHANGE_PASSWORD;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }
        });
    }

    //值班签到
    public void signIn(final User user, final String endTimeParameter, final String startWatchtime) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getSignInUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new SignInParameter(user, endTimeParameter, startWatchtime);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.SIGN_IN_EVENT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

        });
    }

    //值班签退
    public void signOut(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getSignOutUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new SignOutParameter(user);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.SIGN_OUT_EVENT;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //获得所有人员
    public void GetAllPatrolMan() {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getAllPatroMenUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetAllPatrolManParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_ALL_PATROL_MAN;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseAllPatroMan(jsonObj);
            }
        });
    }

    //获取外勤车辆
    public void getPatrolBus(final String groupCode, final String busNo, final boolean isMonitor) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getAllPatroBusUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPatrolBusParameter(groupCode, busNo, isMonitor);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_PATROL_BUS;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parsePatrolBusInfo(jsonObj);
            }
        });
    }

    //获得单个人的轨迹
    public void GetPatrolManTrack(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getAllPatroMenUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPatrolManTrackParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_PATROL_MAN_TRACK;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                //@TODO: 解析轨迹private boolean isNum(String str) {
                return JsonUtil.parseAllPatroMan(jsonObj);
            }
        });
    }

    /**
     * 选择组织机构
     *
     * @param user
     */
    public void getGroupsTrees(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getNetGroupsTreeUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPatrolTreeParameter(user);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseOrganisationSelection(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.USER_GET_PATROL_TREE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }


    public void getGroupsTree(final User user) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {
            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }

            @Override
            public String getUrl() {
                return ServiceUrlManager.getInstance().getUserTreeUrl();
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new GetPatrolTreeParameter(user);
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return JsonUtil.parseOrganisationSelection(jsonObj);
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.USER_GET_PATROL_TREE;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }
        });
    }

    //获取常州地震相关服务接口
    public void getMobileConfigCZServicesFile(final String url) {
        RequestExecutor.execute(new ARequestCallback<AServerResponse>() {

            public boolean isForComplexResponse() {
                return false;
            }

            @Override
            public String getUrl() {
                return url;
            }

            @Override
            public IRequestParameter prepareParameters() {
                return new EmptyRequestParameter();
            }

            @Override
            public int getEventId() {
                return ResponseEventStatus.GET_MAPCONFIG_EVENT_CZDZ;
            }

            @Override
            public Class<AServerResponse> getResponseClass() {
                return AServerResponse.class;
            }

            public void onCompletion(short flag, String response) {
                if (flag == -1) {
                    return;
                }
                ResponseEvent event = null;
                if (response == null) {
                    event = new ResponseEvent(getEventId(), ResponseEventStatus.ERROR, ResourceUtil.getStringById(R.string.get_mapconfig_error));
                } else {
                    event = new ResponseEvent(getEventId());
                    event.setStatus(ResponseEventStatus.OK);
                    event.setData(response);
                }
                EventBusUtil.post(event);
            }

            @Override
            protected Object customParse(JSONObject jsonObj) {
                return jsonObj;
            }

            @Override
            protected boolean getParseByGson() {
                return false;
            }

            @Override
            public HttpRequestType getRequestType() {
                return HttpRequestType.GET;
            }
        });
    }
}
