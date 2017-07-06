package com.senacor.codecamp.reactive.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

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


    @Override
    public int hashCode() {
        return Objects.hash(name, content, rating, wordCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WikiArticle other = (WikiArticle) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.content, other.content)
                && Objects.equals(this.rating, other.rating)
                && Objects.equals(this.wordCount, other.wordCount);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("content", content)
                .append("rating", rating)
                .append("wordCount", wordCount)
                .toString();
    }
}
