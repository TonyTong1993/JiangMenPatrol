package com.ecity.cswatersupply.utils;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.ecity.cswatersupply.R;
import com.ecity.cswatersupply.menu.AEventCommandXtd;
import com.ecity.cswatersupply.menu.map.AEventMenu;
import com.ecity.cswatersupply.menu.map.AMapMenu;
import com.ecity.cswatersupply.menu.map.AMapMenuCommand;
import com.ecity.cswatersupply.menu.map.EmptyMapCommandXtd;
import com.z3app.android.util.StringUtil;

public class AnalyzeMapMenuXML {

	public static List<AMapMenu> analyzeMenuXML(Context context) {
        try {
            XmlResourceParser parser = context.getResources().getXml(R.xml.map_btns);
            int eventType = parser.getEventType();
            List<AMapMenu> menuLists = null;
            AMapMenu appMenu = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Category".equalsIgnoreCase(tagName)) {
                            menuLists = new ArrayList<AMapMenu>();
                        } else if ("Button".equalsIgnoreCase(tagName)) {
                            appMenu = new AMapMenu();
                        } else if ("Name".equalsIgnoreCase(tagName)) {
                            appMenu.setName(parser.nextText());
                        } else if ("IconName".equalsIgnoreCase(tagName)) {
                            appMenu.setIconName(parser.nextText());
                        } else if ("Command".equalsIgnoreCase(tagName)) {
                            appMenu.setMapMenuCommand(getAMapMenuCommand(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Button".equalsIgnoreCase(tagName)) {
                            if (appMenu != null) {
                                menuLists.add(appMenu);
                            }
                            appMenu = null;
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
            return menuLists;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;

    }
	
	public static List<AEventMenu> analyzeMenuXML(Context context,int xmlId) {
        try {
            XmlResourceParser parser = context.getResources().getXml(xmlId);
            int eventType = parser.getEventType();
            List<AEventMenu> menuLists = null;
            AEventMenu appMenu = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("Category".equalsIgnoreCase(tagName)) {
                            menuLists = new ArrayList<AEventMenu>();
                        } else if ("Menu".equalsIgnoreCase(tagName)) {
                            appMenu = new AEventMenu();
                        } else if ("Name".equalsIgnoreCase(tagName)) {
                            appMenu.setName(parser.nextText());
                        } else if ("Command".equalsIgnoreCase(tagName)) {
                            appMenu.setEventCommand(getAEventMenuCommand(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Menu".equalsIgnoreCase(tagName)) {
                            if (appMenu != null) {
                                menuLists.add(appMenu);
                            }
                            appMenu = null;
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();
            }
            return menuLists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
	
	private static AMapMenuCommand getAMapMenuCommand(String text) {
        if (StringUtil.isEmpty(text)) {
            return new EmptyMapCommandXtd();
        }
        AMapMenuCommand command = null;
        try {
            command = (AMapMenuCommand) Class.forName(text).newInstance();
            return command;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new EmptyMapCommandXtd();
    }
	
	private static AEventCommandXtd getAEventMenuCommand(String text) {
        if (StringUtil.isEmpty(text)) {
            return null;
        }
        AEventCommandXtd command = null;
        try {
            command = (AEventCommandXtd) Class.forName(text).newInstance();
            return command;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
