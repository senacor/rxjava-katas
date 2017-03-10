package com.senacor.tecco.codecamp.reactive.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Andreas Keefer
 */
public class WordCount implements Serializable {

    private final String articleName;
    private final int count;

    @JsonCreator
    public WordCount(@JsonProperty("articleName") String articleName,
                     @JsonProperty("count") int count) {
        this.articleName = articleName;
        this.count = count;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName, count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WordCount other = (WordCount) obj;
        return Objects.equals(this.articleName, other.articleName)
                && Objects.equals(this.count, other.count);
    }

    @Override
    public String toString() {
        return "WordCount{" +
                "articleName='" + articleName + '\'' +
                ", count=" + count +
                '}';
    }
}
