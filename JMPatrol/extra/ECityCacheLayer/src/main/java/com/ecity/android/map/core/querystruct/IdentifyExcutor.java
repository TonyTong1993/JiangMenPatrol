package com.ecity.android.map.core.querystruct;

import com.esri.core.tasks.ags.identify.IdentifyParameters;

public class IdentifyExcutor {

    private String             mUrl;
    private IdentifyParameters parameters;
    private String             mDbPath;

    public IdentifyExcutor(String url, String dbPath, IdentifyParameters param) {
        this.mUrl = url;
        this.parameters = param;
        this.mDbPath = dbPath;
    }

    public IdentiResult[] getIdentiResults() throws Exception {
        LocalIdentifyTask task = new LocalIdentifyTask(mDbPath);
        return task.excute(parameters);
    }
}
