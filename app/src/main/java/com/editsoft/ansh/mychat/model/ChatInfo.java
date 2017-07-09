package com.editsoft.ansh.mychat.model;

/**
 * Created by SurvivoR on 7/10/2017.
 */

public class ChatInfo {
    private String id;
    private String name;
    private String mobileNo;
    private String msg;
    private String cardType;
    private String imageUrl;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    /*  public ChatInfo(String id, String name, String mobileNo, String msg, String cardType) {
        this.id = id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.msg = msg;
        this.cardType = cardType;
    }*/

    public ChatInfo(String id, String name, String mobileNo, String msg, String cardType, String imageUrl) {
        this.id = id;
        this.name = name;
        this.mobileNo = mobileNo;
        this.msg = msg;
        this.cardType = cardType;
        this.imageUrl = imageUrl;
    }

    public ChatInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
