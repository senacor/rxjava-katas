package com.senacor.tecco.codecamp.reactive.service;

/**
 * @author Andri Bremm
 */
public final class WikiArticle {

    private final String name;
    private final String content;
    private final int rating;
    private final int wordCount;

    public WikiArticle(String name, String content, int rating, int wordCount) {
        this.name = name;
        this.content = content;
        this.rating = rating;
        this.wordCount = wordCount;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public int getWordCount() {
        return wordCount;
    }

}
