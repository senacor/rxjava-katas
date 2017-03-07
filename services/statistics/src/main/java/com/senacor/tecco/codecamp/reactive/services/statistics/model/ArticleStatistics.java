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

    @JsonCreator
    public ArticleStatistics(@JsonProperty("articleCount") int articleCount, @JsonProperty("wordCountAvg") double wordCountAvg,
                             @JsonProperty("ratingAvg") double ratingAvg) {
        this.articleCount = articleCount;
        this.wordCountAvg = wordCountAvg;
        this.ratingAvg = ratingAvg;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleStatistics that = (ArticleStatistics) o;
        return articleCount == that.articleCount &&
                Double.compare(that.wordCountAvg, wordCountAvg) == 0 &&
                Double.compare(that.ratingAvg, ratingAvg) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleCount, wordCountAvg, ratingAvg);
    }

    @Override
    public String toString() {
        return "ArticleStatistics{" +
                "articleCount=" + articleCount +
                ", wordCountAvg=" + wordCountAvg +
                ", ratingAvg=" + ratingAvg +
                '}';
    }
}
