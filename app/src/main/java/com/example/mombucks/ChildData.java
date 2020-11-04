package com.example.mombucks;

public class ChildData {
    public String childProfile;
    public String childName;
    public String childBalance;

    public String getChildProfile() {
        return childProfile;
    }

    public String getChildName() {
        return childName;
    }

    public String getChildBalance() {
        return childBalance;
    }

    public ChildData(String childProfile, String childName, String childBalance) {
        this.childProfile = childProfile;
        this.childName = childName;
        this.childBalance = childBalance;
    }
    public ChildData(String childName, String childProfile ) {
        this.childProfile = childProfile;
        this.childName = childName;
    }
}

