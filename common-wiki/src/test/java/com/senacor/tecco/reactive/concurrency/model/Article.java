package com.senacor.tecco.reactive.concurrency.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by mmenzel on 27.01.2016.
 */
public class Article {
    public String name;
    public String content;

    public Article(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", name)
                .append("content", content)
                .toString();
    }
}