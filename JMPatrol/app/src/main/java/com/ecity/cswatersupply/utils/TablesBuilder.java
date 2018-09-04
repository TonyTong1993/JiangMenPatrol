package com.ecity.cswatersupply.utils;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.Resources;

import com.ecity.android.db.model.FieldModel;
import com.ecity.android.db.model.TableModel;
import com.ecity.android.db.utils.StringUtil;

public class TablesBuilder {
	private static List<TableModel> tables = new ArrayList<TableModel>();
	public static void analyzeTablesXML(Resources resources)
	{
		XmlPullParser parser = resources.getXml(ResourceUtil.getXmlResourceId("tables"));
		try {
			parseFromXML(parser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void parseFromXML(XmlPullParser parser) throws Exception {
		TableModel table = null;
		List<FieldModel> fields = null;
		FieldModel field = null;
		//XmlPullParser parser = Xml.newPullParser();
		//parser.setInput(inStream, "UTF-8");

		if( null == parser)
			return;
		
		int eventType = parser.getEventType();// 产生第一个事件
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				if(null  == tables)
					tables = new ArrayList<TableModel>();
				tables.clear();
				break;
			case XmlPullParser.START_TAG:
				String name = parser.getName();// 获取解析器当前指向的元素的名称
				if ("Table".equalsIgnoreCase(name)) {
					table = new TableModel();
					fields = new ArrayList<FieldModel>();
					table.setName(parser.getAttributeValue(0));
				}
				if ("Field".equalsIgnoreCase(name)) {
					field = new FieldModel();
				}

				if(field !=null){
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
						if(tmp.isEmpty() || "".equalsIgnoreCase(tmp))
							field.setLength(0);						
						else
							field.setLength(Integer.parseInt(tmp));
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
						tables.add(table);
					}
					fields = null;
				}
				if ("Field".equalsIgnoreCase(parser.getName())) {
					if (field != null && fields != null)
						fields.add(field);
					field = null;
				}
				break;
			}
			eventType = parser.next();
		}
	}
	/***
	 * 根据表名获得表模型
	 * @param tbname
	 * @return
	 */
	public static TableModel getTableByName(String tbname)
	{
		TableModel result = null;
		if(StringUtil.isEmpty(tbname))
			return result;
		
		for (TableModel table : tables) {
			if(tbname.equalsIgnoreCase(table.getName()))
			{
				result = table;
				break;
			}
		}
		return result;
	}
}

