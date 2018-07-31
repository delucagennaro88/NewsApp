package com.example.utente.newsapp;

public class News {
    private String mArticleTitle;
    private String mArticleCategory;
    private String mArticleDate;
    private String mArticleUrl;
    private String mArticleAuthor;

    public News(String mArticleTitle, String mArticleCategory, String mArticleDate, String mArticleUrl, String mArticleAuthor) {
        this.mArticleTitle = mArticleTitle;
        this.mArticleCategory = mArticleCategory;
        this.mArticleDate = mArticleDate;
        this.mArticleUrl = mArticleUrl;
        this.mArticleAuthor = mArticleAuthor;
    }

    public String getmArticleTitle() {
        return mArticleTitle;
    }

    public String getmArticleCategory() {
        return mArticleCategory;
    }

    public String getmArticleDate() {
        return mArticleDate;
    }

    public String getmArticleUrl() {
        return mArticleUrl;
    }

    public String getmArticleAuthor() {
        return mArticleAuthor;
    }
}
