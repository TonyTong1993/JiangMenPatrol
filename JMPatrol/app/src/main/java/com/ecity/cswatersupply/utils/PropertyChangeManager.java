package com.ecity.cswatersupply.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import com.ecity.cswatersupply.HostApplication;
import com.ecity.cswatersupply.model.PropertyChangeModel;
import com.z3app.android.util.PreferencesUtil;

public class PropertyChangeManager {
    private volatile static PropertyChangeManager instance;

    private ArrayList<PropertyChangeListener> listeners;// 辅助，用于全部删除
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private PropertyChangeManager() {
        listeners = new ArrayList<PropertyChangeListener>();
    }

    public static PropertyChangeManager getInstance() {
        if (instance == null) {
            synchronized (PropertyChangeManager.class) {
                if (instance == null) {
                    instance = new PropertyChangeManager();
                }
            }
        }
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listeners != null) {
            listeners.add(listener);
        }
        changes.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
        changes.removePropertyChangeListener(listener);
    }

    public void removeAllListeners() {
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                changes.removePropertyChangeListener(listeners.get(i));
            }
            listeners.clear();// 别忘记这一步
        }
    }

    public void updateNotificationCountByKey(String key) {
        updateNotificationCountByKey(key, true, 1);// 默认+1
    }

    public void clearNoticationByKey(String key) {
        int unreadCount = getExistingCount(key);
        setNotificationUnreadCount(key, unreadCount, 0);
    }

    /**
     * @param key
     * @param increase
     * @param deltaCount 数量增量
     */
    private void updateNotificationCountByKey(String key, boolean increase, int deltaCount) {
        int unreadCount = getExistingCount(key);
        int count = increase ? (unreadCount + deltaCount) : (unreadCount - deltaCount);
        setNotificationUnreadCount(key, unreadCount, count);
    }

    private void setNotificationUnreadCount(String key, int existingCount, int newValue) {
        PropertyChangeModel existingValueModel = new PropertyChangeModel(key, existingCount);
        PropertyChangeModel newValueModel = new PropertyChangeModel(key, newValue);
        PreferencesUtil.putInt(HostApplication.getApplication().getApplicationContext(), key + RestoreManager.getInstance().restoreLastUser().getId(), newValue);
        changes.firePropertyChange("PropertyChangeModel", existingValueModel, newValueModel);
    }

    private int getExistingCount(String key) {
        return PreferencesUtil.getInt(HostApplication.getApplication().getApplicationContext(), key + RestoreManager.getInstance().restoreLastUser().getId(), -1);
    }
}
