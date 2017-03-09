package com.senacor.tecco.codecamp.reactive.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ArticleStatistics {

    private final int articleCount;
    private final double wordCountAvg;
    private final double ratingAvg;
    private final double fetchTimeInMillisAvg;

    @JsonCreator
    public ArticleStatistics(@JsonProperty("articleCount") int articleCount,
                             @JsonProperty("wordCountAvg") double wordCountAvg,
                             @JsonProperty("ratingAvg") double ratingAvg,
                             @JsonProperty("fetchTimeInMillisAvg") double fetchTimeInMillisAvg) {
        this.articleCount = articleCount;
        this.wordCountAvg = wordCountAvg;
        this.ratingAvg = ratingAvg;
        this.fetchTimeInMillisAvg = fetchTimeInMillisAvg;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public double getWordCountAvg() {
        return wordCountAvg;
    }

    public double getRatingAvg() {
        return ratingAvg;
    }

    public double getFetchTimeInMillisAvg() {
        return fetchTimeInMillisAvg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleCount, wordCountAvg, ratingAvg, fetchTimeInMillisAvg);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ArticleStatistics other = (ArticleStatistics) obj;
        return Objects.equals(this.articleCount, other.articleCount)
                && Objects.equals(this.wordCountAvg, other.wordCountAvg)
                && Objects.equals(this.ratingAvg, other.ratingAvg)
                && Objects.equals(this.fetchTimeInMillisAvg, other.fetchTimeInMillisAvg);
    }

    @Override
    public String toString() {
        return "ArticleStatistics{" +
                "articleCount=" + articleCount +
                ", wordCountAvg=" + wordCountAvg +
                ", ratingAvg=" + ratingAvg +
                ", fetchTimeInMillisAvg=" + fetchTimeInMillisAvg +
                '}';
    }
}
