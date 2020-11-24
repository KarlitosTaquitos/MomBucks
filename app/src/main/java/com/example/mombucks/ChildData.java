package com.example.mombucks;

public class ChildData
{
    public String childProfile;//create the required fields here now
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

    public ChildData(String childName ,String childBalance,String childProfile ) {
        this.childProfile = childProfile;
        this.childName = childName;
        this.childBalance = childBalance;
        
    }
    public ChildData(String childName, String childProfile ) {
        this.childProfile = childProfile;
        this.childName = childName;
    }
}
