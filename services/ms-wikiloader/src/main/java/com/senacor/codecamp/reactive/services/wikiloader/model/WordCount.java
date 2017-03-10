package com.senacor.codecamp.reactive.services.wikiloader.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("articleName", articleName)
                .append("count", count)
                .toString();
    }
}
