package com.example.skymail.Data;

public class Contacts {
    private String userID;
    private String UserFullname;
    private String UserEmail;
    private String UserProfilePicURI;

    public Contacts() {
    }

    public Contacts(String userID, String userFullname, String userEmail, String userProfilePicURI) {
        this.userID = userID;
        UserFullname = userFullname;
        UserEmail = userEmail;
        UserProfilePicURI = userProfilePicURI;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFullname() {
        return UserFullname;
    }

    public void setUserFullname(String userFullname) {
        UserFullname = userFullname;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserProfilePicURI() {
        return UserProfilePicURI;
    }

    public void setUserProfilePicURI(String userProfilePicURI) {
        UserProfilePicURI = userProfilePicURI;
    }
}
