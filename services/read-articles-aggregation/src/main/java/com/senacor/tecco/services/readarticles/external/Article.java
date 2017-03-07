package com.senacor.tecco.services.readarticles.external;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class Article {

    private final String name, content;

    private Article(){
        name = content = null;
    }

    public Article(String name, String text) {
        this.name = name;
        this.content = text;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
