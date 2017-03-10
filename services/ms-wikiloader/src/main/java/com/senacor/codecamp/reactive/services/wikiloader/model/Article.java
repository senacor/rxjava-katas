package com.senacor.codecamp.reactive.services.wikiloader.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.abbreviateWithoutNewline;

/**
 * @author Andreas Keefer
 */
public class Article implements Serializable {

    public interface NameOnly {
    }

    @JsonView(NameOnly.class)
    private final String name;
    private final String content;
    @JsonView(NameOnly.class)
    private final Integer fetchTimeInMillis;

    @JsonCreator
    public Article(@JsonProperty("name") String name,
                   @JsonProperty("content") String content,
                   @JsonProperty("fetchTimeInMillis") Integer fetchTimeInMillis) {
        this.name = name;
        this.content = content;
        this.fetchTimeInMillis = fetchTimeInMillis;
    }

    private Article(Builder builder) {
        name = builder.name;
        content = builder.content;
        fetchTimeInMillis = builder.fetchTimeInMillis;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Article copy) {
        Builder builder = new Builder();
        builder.name = copy.name;
        builder.content = copy.content;
        builder.fetchTimeInMillis = copy.fetchTimeInMillis;
        return builder;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Integer getFetchTimeInMillis() {
        return fetchTimeInMillis;
    }

    public ArticleName toArticleName(){
        return new ArticleName(getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Article other = (Article) obj;
        return Objects.equals(this.name, other.name)
                && Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("content", abbreviateWithoutNewline(content, 50))
                .append("fetchTimeInMillis", fetchTimeInMillis)
                .toString();
    }

    public static final class Builder {
        private String name;
        private String content;
        private Integer fetchTimeInMillis;

        private Builder() {
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withContent(String val) {
            content = val;
            return this;
        }

        public Builder withFetchTimeInMillis(Integer val) {
            fetchTimeInMillis = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}
