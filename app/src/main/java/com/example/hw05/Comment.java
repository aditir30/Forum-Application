/*
 Assignment : HW 05
 File Name : Comment.java
 Student Full Name: Aditi Raghuwanshi, Pratik Chaudhari
 */
package com.example.hw05;

import com.google.firebase.Timestamp;

public class Comment {
    private String CreatedBy;
    private com.google.firebase.Timestamp CreatedAt;
    private String CommentDescription;
    private String CommentID;

    public Comment(String createdBy,com.google.firebase.Timestamp createdAt, String commentDescription, String commentID) {
        CreatedBy = createdBy;
        CommentDescription = commentDescription;
        CommentID = commentID;
        CreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCommentDescription() {
        return CommentDescription;
    }

    public String getCommentID() {
        return CommentID;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "CreatedBy='" + CreatedBy + '\'' +
                ", CreatedAt=" + CreatedAt +
                ", CommentDescription='" + CommentDescription + '\'' +
                ", CommentID='" + CommentID + '\'' +
                '}';
    }
}
