package com.example.utente.newsapp;

import java.util.Date;

class News {

    private final String mArticleTitle;

    private final String mArticleSection;

    private final String mArticleAuthor;

    private final Date mArticleDate;

    private final String mArticleUrl;

    private final String mArticleImage;

    public News(String title, String section, String journalist, Date publication, String url, String pic) {
        mArticleTitle = title;
        mArticleSection = section;
        mArticleAuthor = journalist;
        mArticleDate = publication;
        mArticleUrl = url;
        mArticleImage = pic;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public String getArticleSection() {
        return mArticleSection;
    }

    public String getArticleAuthor() {
        return mArticleAuthor;
    }

    public Date getArticleDate() {
        return mArticleDate;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public String getArticleImage() {
        return mArticleImage;
    }
}