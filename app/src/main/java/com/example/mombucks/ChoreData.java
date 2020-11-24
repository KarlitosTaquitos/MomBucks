package com.example.mombucks;

public class ChoreData {

   public String choreName;
   public String description;

    public ChoreData(String choreName, String description) {
        this.choreName = choreName;
        this.description = description; }

    public String getChoreName() {
        return choreName; }

    public String getDescription() {
        return description; }
}
