package com.messenger.models;

import android.graphics.drawable.Drawable;

public class DeviceApp {
    private String appId;
    private String name;
    private Drawable icon;
    private boolean isCheck;

    public DeviceApp(String appId, String name, Drawable icon, boolean isCheck) {
        this.appId = appId;
        this.name = name;
        this.icon = icon;
        this.isCheck = isCheck;
    }

    public DeviceApp() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
