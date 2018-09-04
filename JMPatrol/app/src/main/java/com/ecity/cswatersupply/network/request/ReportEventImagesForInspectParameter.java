package com.ecity.cswatersupply.network.request;

import java.util.HashMap;
import java.util.Map;

import com.ecity.cswatersupply.network.RequestParameter.IRequestParameter;

public class ReportEventImagesForInspectParameter implements IRequestParameter {

	private String ids;
	private String imageName;

	public ReportEventImagesForInspectParameter(String ids,
			String imageName) {
		this.ids = ids;
		this.imageName = imageName;
	}

	@Override
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ids", ids);
		map.put("imagename", imageName);
		return map;
	}
}
