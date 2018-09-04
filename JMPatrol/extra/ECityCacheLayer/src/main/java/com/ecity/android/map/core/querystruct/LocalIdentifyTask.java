package com.ecity.android.map.core.querystruct;

import com.ecity.android.map.core.dbquery.DbQueryOrganizer;
import com.ecity.android.map.core.local.ECityPipeDbQueryer;
import com.ecity.android.map.core.local.DbQueryParam;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

import java.util.Iterator;
import java.util.List;

public class LocalIdentifyTask {

    private String dbPath;

    public LocalIdentifyTask(String dbPath) {
        this.dbPath = dbPath;
    }

    public LocalIdentiResult[] excute(IdentifyParameters param) {

        // 得到查询参数。
        DbQueryParam queryParam= DbQueryOrganizer.organzeIdentifyClause(param);
        List<Graphic> graphics = ECityPipeDbQueryer.getInstance().queryByParam(
                queryParam);

        if (graphics != null) {
            LocalIdentiResult results[] = new LocalIdentiResult[graphics.size()];
            int i = 0;
            for (Iterator iterator = graphics.iterator(); iterator.hasNext();) {
                Graphic graphic = (Graphic) iterator.next();
                LocalIdentiResult result = new LocalIdentiResult();
                result.setGraphic(graphic);
                results[i] = result;
                i++;
            }
            return results;
        }
        return null;
    }
}
