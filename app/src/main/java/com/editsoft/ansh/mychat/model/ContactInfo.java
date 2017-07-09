package com.editsoft.ansh.mychat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SurvivoR on 7/10/2017.
 */

public class ContactInfo implements Parcelable {
    private String chatId;
    private String name;
    private String mobileNo;


    public ContactInfo(String chatId, String name, String mobileNo) {
        this.chatId = chatId;
        this.name = name;
        this.mobileNo = mobileNo;

    }


    public ContactInfo() {

    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chatId);
        dest.writeString(this.name);
        dest.writeString(this.mobileNo);

    }

    protected ContactInfo(Parcel in) {
        this.chatId = in.readString();
        this.name = in.readString();
        this.mobileNo = in.readString();

    }

    public static final Parcelable.Creator<ContactInfo> CREATOR = new Parcelable.Creator<ContactInfo>() {
        @Override
        public ContactInfo createFromParcel(Parcel source) {
            return new ContactInfo(source);
        }

        @Override
        public ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };
}
