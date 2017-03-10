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
public class Rating implements Serializable {
    private final String articleName;
    private final int rating;

    @JsonCreator
    public Rating(@JsonProperty("articleName") String articleName,
                  @JsonProperty("rating") int rating) {
        this.articleName = articleName;
        this.rating = rating;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName, rating);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Rating other = (Rating) obj;
        return Objects.equals(this.articleName, other.articleName)
                && Objects.equals(this.rating, other.rating);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("articleName", articleName)
                .append("rating", rating)
                .toString();
    }
}
