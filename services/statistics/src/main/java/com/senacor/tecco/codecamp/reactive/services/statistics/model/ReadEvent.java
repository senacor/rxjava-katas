package com.senacor.tecco.codecamp.reactive.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ReadEvent {

    private final String articleName;
    private final int wordCount;
    private final int rating;

    @JsonCreator
    public ReadEvent(@JsonProperty("articleName") String articleName, @JsonProperty("wordCount") int wordCount,
                             @JsonProperty("rating") int rating) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadEvent readEvent = (ReadEvent) o;
        return wordCount == readEvent.wordCount &&
                rating == readEvent.rating &&
                Objects.equals(articleName, readEvent.articleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName, wordCount, rating);
    }
}
