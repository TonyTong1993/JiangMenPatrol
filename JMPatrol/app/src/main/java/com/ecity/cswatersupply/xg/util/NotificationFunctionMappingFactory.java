package com.ecity.cswatersupply.xg.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;

import com.ecity.cswatersupply.utils.ResourceUtil;

/**
 * @author Ma Qianli
 *
 */
public class NotificationFunctionMappingFactory {
    public static final String TAG = "NotificationFunctionMappingFactory";
    private static Map<String, Class<?>> map = new HashMap<String, Class<?>>();

    private NotificationFunctionMappingFactory() {
    }

    public static Class<?> getFunctionHandlerClass(String notificationType) {
        Iterator<Entry<String, Class<?>>> it = map.entrySet().iterator();
         Map.Entry<String, Class<?>> entry = null;
        String types = null;
        while (it.hasNext()) {
            entry = it.next();
            types = entry.getKey();
            if (types.contains(notificationType)) {
                return map.get(types);
            }
        }

        return null;
    }

    public static void parseConfigXml(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseInBackground(context);
            }
        }).start();
    }

    private static void parseInBackground(Context context) {
        //parser.setInput(inStream, "UTF-8");
        try {
            XmlPullParser parser = context.getResources().getXml(ResourceUtil.getXmlResourceId("notification_function_mapping"));
            String notificationType = null;
            int eventType = parser.getEventType();// 产生第一个事件
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        map.clear();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if ("NotificationType".equalsIgnoreCase(name)) {
                            notificationType = parser.nextText();
                        } else if ("Function".equalsIgnoreCase(name)) {
                            Class<?> cls = Class.forName(parser.nextText());
                            map.put(notificationType, cls);
                        } else if ((name == null) || (!"Mapping".equalsIgnoreCase(name) && !"Mappings".equalsIgnoreCase(name))) {
                            throw new RuntimeException("Invalid tag name '" + name + "' in notification_function_mapping.xml.");
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
