package com.messenger.models;

public class MessApp {
    private int id;
    private String name;
    private String url;
    private String icon;
    private long openCount;
    private long openTime;
    private Boolean check;
    private String usename;
    private long timeLimit;

    public MessApp(int id, String name, String url, String icon, long openCount, long openTime, Boolean check, String usename, long timeLimit) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.openCount = openCount;
        this.openTime = openTime;
        this.check = check;
        this.usename = usename;
        this.timeLimit = timeLimit;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public MessApp() {
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getOpenCount() {
        return openCount;
    }

    public void setOpenCount(long openCount) {
        this.openCount = openCount;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }
}
