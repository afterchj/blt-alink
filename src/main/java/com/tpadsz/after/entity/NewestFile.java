package com.tpadsz.after.entity;

/**
 * Created by chenhao.lu on 2018/6/7.
 */
public class NewestFile {

    private String appId;
    private Integer versionCode;
    private String pkg;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }
}
