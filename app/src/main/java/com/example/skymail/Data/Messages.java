package com.example.skymail.Data;



public class Messages {
    String userID;
    String messagID;
    String From;
    String To;
    String Subject;
    String Object;
    String MessageText;
    String SenderProfilePicture;

    public Messages() {
    }

    public Messages(String userID, String messagID, String from, String to, String subject, String object, String messageText, String senderProfilePicture) {
        this.userID = userID;
        this.messagID = messagID;
        From = from;
        To = to;
        Subject = subject;
        Object = object;
        MessageText = messageText;
        SenderProfilePicture = senderProfilePicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessagID() {
        return messagID;
    }

    public void setMessagID(String messagID) {
        this.messagID = messagID;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public String getSenderProfilePicture() {
        return SenderProfilePicture;
    }

    public void setSenderProfilePicture(String senderProfilePicture) {
        SenderProfilePicture = senderProfilePicture;
    }
}