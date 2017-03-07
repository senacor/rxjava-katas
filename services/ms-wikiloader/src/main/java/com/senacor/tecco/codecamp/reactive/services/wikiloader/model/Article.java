package com.senacor.tecco.codecamp.reactive.services.wikiloader.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

import static com.senacor.tecco.reactive.util.ReactiveUtil.abbreviateWithoutNewline;

/**
 * @author Andreas Keefer
 */
public class Article implements Serializable {

    private final String name;
    private final String content;

    @JsonCreator
    public Article(@JsonProperty("name") String name,
                   @JsonProperty("content") String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
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
                .toString();
    }
}
