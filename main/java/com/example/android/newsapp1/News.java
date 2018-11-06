package com.example.android.newsapp1;


public class News {

    private String title;
    private String url;
    private String publishedDate;
    private String authorName;
    private String section;

    /**
     * Create a new Earthquake object. (the constructor)
     *
     * @param title           title
     * @param url            url
     * @param publishedDate        date
     * @param authorName           name
     * @param section             section
     */

    public News(String title, String url, String publishedDate, String authorName, String section) {
        this.title = title;
        this.url = url;
        this.publishedDate = publishedDate;
        this.authorName = authorName;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getSection() {
        return section;
    }

}
