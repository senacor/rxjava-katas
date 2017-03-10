package com.senacor.codecamp.reactive.services.statistics.model;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ArticleMetrics {

    private final int rating;
    private final int wordCount;
    private final Integer fetchTimeInMillis;

    public ArticleMetrics(int rating, int wordCount, Integer fetchTimeInMillis) {
        this.rating = rating;
        this.wordCount = wordCount;
        this.fetchTimeInMillis = fetchTimeInMillis;
    }

    public Integer getFetchTimeInMillis() {
        return fetchTimeInMillis;
    }

    public int getRating() {
        return rating;
    }

    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, wordCount, fetchTimeInMillis);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ArticleMetrics other = (ArticleMetrics) obj;
        return Objects.equals(this.rating, other.rating)
                && Objects.equals(this.wordCount, other.wordCount)
                && Objects.equals(this.fetchTimeInMillis, other.fetchTimeInMillis);
    }

    @Override
    public String toString() {
        return "ArticleMetrics{" +
                "rating=" + rating +
                ", wordCount=" + wordCount +
                ", fetchTimeInMillis=" + fetchTimeInMillis +
                '}';
    }
}
