package com.zzz.ecity.android.applibrary.database;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.Resources;

import com.ecity.android.db.model.FieldModel;
import com.ecity.android.db.model.TableModel;
import com.ecity.android.db.utils.StringUtil;
import com.zzz.ecity.android.applibrary.R;

public class TablesBuilder {
	 protected static final  ConcurrentHashMap<String, TableModel> tables = new ConcurrentHashMap<String, TableModel>();
	/***
	 * 初始化默认数据表
	 * @param resources
	 */
	public static void analyzeTablesXML(Resources resources) {
		analyzeTablesXML(resources, R.xml.positiontable);
	}
	/***
	 * 解析资源文件夹中的数据表xml文件
	 * @param resources 资源管理器对象
	 * @param resourceId 文件资源id
	 */
	public static void analyzeTablesXML(Resources resources, int resourceId) {
		TableModel table = null;
		List<FieldModel> fields = null;
		FieldModel field = null;

		XmlPullParser parser = resources.getXml(resourceId);
		try {
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("Table".equalsIgnoreCase(name)) {
						table = new TableModel();
						fields = new ArrayList<FieldModel>();
						table.setName(parser.getAttributeValue(0));
					}
					if ("Field".equalsIgnoreCase(name)) {
						field = new FieldModel();
					}

					if (field != null) {
						if ("name".equalsIgnoreCase(name)) {
							field.setName(parser.nextText());
						}
						if ("type".equalsIgnoreCase(name)) {
							field.setType(parser.nextText());
						}
						if ("alias".equalsIgnoreCase(name)) {
							field.setAlias(parser.nextText());
						}
						if ("length".equalsIgnoreCase(name)) {
							String tmp = parser.nextText();
							if (com.z3app.android.util.StringUtil.isBlank(tmp)) {
								field.setLength(0);
							} else {
								field.setLength(Integer.parseInt(tmp));
							}
						}
						if ("default".equalsIgnoreCase(name)) {
							field.setDefaultvalue(parser.nextText());
						}
						if ("SelValue".equalsIgnoreCase(name)) {
							field.setSelValue(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("Table".equalsIgnoreCase(parser.getName())) {
						if (table != null && fields != null) {
							table.setFields(fields);
							tables.put(table.getName(), table);
						}
						fields = null;
					}
					if ("Field".equalsIgnoreCase(parser.getName())) {
						if (field != null && fields != null) {
							fields.add(field);
						}
						field = null;
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static TableModel getTableByName(String tableName) {
		if (StringUtil.isEmpty(tableName)) {
			return null;
		}
		
		if(null == tables){
			return null;
		}
		
		if(tables.containsKey(tableName)){
			return tables.get(tableName);
		}
		
		return null;
	}
}
