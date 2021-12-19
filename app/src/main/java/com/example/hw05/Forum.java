package com.example.hw05;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.io.Serializable;
import java.util.ArrayList;


public class Forum implements Serializable {

    private com.google.firebase.Timestamp CreatedAt;
    private String CreatedBy;
    private String ForumDescription;
    private String ForumTitle;

    public String getForumID() {
        return ForumID;
    }

    private String ForumID;
    private ArrayList<String> LikedBy;

    public Forum(String forumID,com.google.firebase.Timestamp createdAt, String createdBy, String forumDescription, String forumTitle, ArrayList<String> likedBy) {
        CreatedAt = createdAt;
        CreatedBy = createdBy;
        ForumDescription = forumDescription;
        ForumTitle = forumTitle;
        LikedBy = likedBy;
        ForumID = forumID;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "CreatedAt=" + CreatedAt +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", ForumDescription='" + ForumDescription + '\'' +
                ", ForumTitle='" + ForumTitle + '\'' +
                ", LikedBy=" + LikedBy +
                '}';
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getForumDescription() {
        return ForumDescription;
    }

    public String getForumTitle() {
        return ForumTitle;
    }

    public ArrayList<String> getLikedBy() {
        return LikedBy;
    }
}
