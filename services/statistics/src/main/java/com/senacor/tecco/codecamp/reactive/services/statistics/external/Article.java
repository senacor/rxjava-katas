package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class Article {

    private final String name, content;

    @JsonCreator
    public Article(@JsonProperty("name") String name, @JsonProperty("content") String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
