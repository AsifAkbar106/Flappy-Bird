package com.example.mygame;

public class ReviewModel {
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String comment;
    private String username;



    public ReviewModel(int rating, String comment, String username) {
        this.rating = rating;
        this.comment = comment;
        this.username = username;
    }
}

