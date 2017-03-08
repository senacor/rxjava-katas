package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ArticleReadEvent {

    private final String articleName;

    @JsonCreator
    public ArticleReadEvent(@JsonProperty("articleName") String articleName) {
        this.articleName = articleName;
    }

    public String getArticleName() {
        return articleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleReadEvent that = (ArticleReadEvent) o;
        return Objects.equals(articleName, that.articleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName);
    }

    @Override
    public String toString() {
        return "ArticleReadEvent{" +
                "articleName='" + articleName + '\'' +
                '}';
    }
}
