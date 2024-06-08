package com.example.dancestudioapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Masterclass implements Parcelable {
    private Date dateObject;
    private String id, name, choreograph, hallName;
    private int maxMembersNumber;
    private List<String> memberIds = new ArrayList<>();

    public Masterclass(){}

    public Masterclass(Date dateObject, String name, String choreographer, String hallName, int maxMembersNumber) {
        this.dateObject = dateObject;
        this.name = name;
        this.choreograph = choreographer;
        this.hallName = hallName;
        this.maxMembersNumber = maxMembersNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    protected Masterclass(Parcel in) {
        long tmpDate = in.readLong();
        this.dateObject = tmpDate == -1 ? null : new Date(tmpDate);
        id = in.readString();
        name = in.readString();
        choreograph = in.readString();
        hallName = in.readString();
        maxMembersNumber = in.readInt();
        memberIds = in.createStringArrayList();
    }

    public static final Creator<Masterclass> CREATOR = new Creator<Masterclass>() {
        @Override
        public Masterclass createFromParcel(Parcel in) {
            return new Masterclass(in);
        }

        @Override
        public Masterclass[] newArray(int size) {
            return new Masterclass[size];
        }
    };

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }

    public String getName() {
        return name;
    }

    public String getChoreograph() {
        return choreograph;
    }

    public String getHallName() {
        return hallName;
    }

    public int getMaxMembersNumber() {
        return maxMembersNumber;
    }

    public void setMember(String memberName) {
        memberIds.add(memberName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(dateObject != null ? dateObject.getTime() : -1);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(choreograph);
        dest.writeString(hallName);
        dest.writeInt(maxMembersNumber);
        dest.writeStringList(memberIds);
    }
}

