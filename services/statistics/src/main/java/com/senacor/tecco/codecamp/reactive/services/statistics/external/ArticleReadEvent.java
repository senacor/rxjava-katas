package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleName;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class ArticleReadEvent {

    private final String articleName;
    private final Integer fetchTimeInMillis;

    @JsonCreator
    public ArticleReadEvent(@JsonProperty("name") String articleName,
                            @JsonProperty("fetchTimeInMillis") Integer fetchTimeInMillis) {
        this.articleName = articleName;
        this.fetchTimeInMillis = fetchTimeInMillis;
    }

    public String getArticleName() {
        return articleName;
    }

    public Integer getFetchTimeInMillis() {
        return fetchTimeInMillis;
    }

    public ArticleName toArticleName() {
        return new ArticleName(getArticleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName, fetchTimeInMillis);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ArticleReadEvent other = (ArticleReadEvent) obj;
        return Objects.equals(this.articleName, other.articleName)
                && Objects.equals(this.fetchTimeInMillis, other.fetchTimeInMillis);
    }

    @Override
    public String toString() {
        return "ArticleReadEvent{" +
                "articleName='" + articleName + '\'' +
                ", fetchTimeInMillis=" + fetchTimeInMillis +
                '}';
    }
}
