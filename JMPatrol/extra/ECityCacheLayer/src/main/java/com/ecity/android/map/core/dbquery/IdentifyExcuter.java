package com.ecity.android.map.core.dbquery;

import com.ecity.android.map.core.querystruct.IdentiResult;
import com.ecity.android.map.core.querystruct.LocalIdentifyTask;
import com.esri.core.tasks.ags.identify.IdentifyParameters;

/**
 * @class name：com.ecity.pipenetpalhd.dbquery
 * @class 测试本地查询执行器
 * @anthor wangfeng
 * @time 2017/6/16 9:56
 */

public class IdentifyExcuter {

	private IdentifyParameters parameters;
	private String             mDbPath;//此处mDbPath暂时未用到，通过FileHelper得到db路径

	public IdentifyExcuter(String dbPath, IdentifyParameters param) {
		this.parameters = param;
		this.mDbPath = dbPath;
	}

	public IdentiResult[] getIdentiResults() throws Exception {

		LocalIdentifyTask task = new LocalIdentifyTask(mDbPath);

		return task.excute(parameters);
	}
}
