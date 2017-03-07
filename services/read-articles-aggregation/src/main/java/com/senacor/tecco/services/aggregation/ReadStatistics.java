package com.senacor.tecco.services.aggregation;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class ReadStatistics {
    private final String articleName;
    private final int wordCount, rating;

    public ReadStatistics(String articleName, int wordCount, int rating) {
        this.articleName = articleName;
        this.wordCount = wordCount;
        this.rating = rating;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getRating() {
        return rating;
    }
}
