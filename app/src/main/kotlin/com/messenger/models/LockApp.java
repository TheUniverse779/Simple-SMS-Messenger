package com.messenger.models;

import android.graphics.drawable.Drawable;

public class LockApp {
    private String appId;
    private String name;
    private Drawable icon;
    private boolean isLock;
    private String pin;
    private long timeOpen;

    public LockApp(String appId, String name, Drawable icon, boolean isLock, String pin, long timeOpen) {
        this.appId = appId;
        this.name = name;
        this.icon = icon;
        this.isLock = isLock;
        this.pin = pin;
        this.timeOpen = timeOpen;
    }

    public LockApp() {
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

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(long timeOpen) {
        this.timeOpen = timeOpen;
    }
}
