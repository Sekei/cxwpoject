package com.cxw.cxwproject.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/2.
 */

public class ChargesBean implements Serializable {
    private String appid;//": "wx0a8cfd71599ccd8a",
    private String partnerid;//": "1407448502",
    private String prepayid;//": "wx20180402110028562d0525b10557656179",
    private String timestamp;//": "1522638028",
    private String noncestr;//": "XRQ82F7AJ3xBTPY5",
    private String sign;//": "B81CE71F3A2244B0B0C8006AB5F76E8A"

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
