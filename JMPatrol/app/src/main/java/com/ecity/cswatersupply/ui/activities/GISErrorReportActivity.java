package com.ecity.cswatersupply.ui.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.view.View;

import com.ecity.cswatersupply.SessionManager;
import com.ecity.cswatersupply.menu.ACommonReportOperator1;
import com.ecity.cswatersupply.model.metaconfig.DbMetaInfo;
import com.ecity.cswatersupply.model.metaconfig.DbMetaSet;
import com.ecity.cswatersupply.model.metaconfig.DbMetaSetConfig;
import com.ecity.cswatersupply.service.ServiceUrlManager;

public class GISErrorReportActivity extends CustomReportActivity1 {
    private ACommonReportOperator1 mCommonReportOperator;
    private static String GISCode = "GISERROR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPatrolTableName();
    }

    public void onBackButtonClicked(View v) {
        finish();
    }

    /**
     * 获取GIS上报的LayerName，即TableName
     */
    private void initPatrolTableName() {
        String patrolUrl = ServiceUrlManager.getInstance().getPatrolFeatureServer();
        downloadPatrolMeta(patrolUrl);
    }

    protected void setCustomReportActivity(GISErrorReportActivity activity) {
        if (null != mCommonReportOperator) {
            mCommonReportOperator.setCustomActivity(activity);
        }
    }

    private void downloadPatrolMeta(final String spatialQueryServiceUrl) {
        new Thread() {
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                String metasUrl = spatialQueryServiceUrl + "/metas?f=json";

                HttpGet httpRequest = new HttpGet(metasUrl);
                HttpResponse httpResponse;
                try {
                    httpResponse = httpclient.execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        DbMetaSetConfig dbMetaSetConfig = DbMetaSetConfig.fromJsonInputStream(httpResponse.getEntity().getContent());
                        DbMetaSet dbMetaSet = dbMetaSetConfig.getDbMetaSet();
                        if (dbMetaSet != null) {
                            List<DbMetaInfo> listDbMetaInfo = new ArrayList<DbMetaInfo>();
                            listDbMetaInfo = dbMetaSet.getListDbMetaInfo();
                            for (int i = 0; i < listDbMetaInfo.size(); i++) {
                                DbMetaInfo tmtinfo = listDbMetaInfo.get(i);
                                if (tmtinfo.getCode().equals(GISCode)) {
                                    SessionManager.GISReportTableName = tmtinfo.getLayername();
                                    SessionManager.GISReportLayerID = tmtinfo.getLayerid();
                                }
                            }
                        }
                    }
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }.start();
    }
}
