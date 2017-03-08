package com.senacor.tecco.codecamp.reactive.services.statistics.model;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ArticleMetrics {

    private final int rating;
    private final int wordCount;

    public ArticleMetrics(int rating, int wordCount) {
        this.rating = rating;
        this.wordCount = wordCount;
    }

    public int getRating() {
        return rating;
    }

    public int getWordCount() {
        return wordCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleMetrics that = (ArticleMetrics) o;
        return rating == that.rating &&
                wordCount == that.wordCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, wordCount);
    }

    @Override
    public String toString() {
        return "ArticleMetrics{" +
                "rating=" + rating +
                ", wordCount=" + wordCount +
                '}';
    }
}
