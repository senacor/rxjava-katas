package com.senacor.codecamp.reactive.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Andri Bremm
 */
public class TopArticle {

    private final String name;
    private final String url;
    private final long reads;

    @JsonCreator
    public TopArticle(@JsonProperty("name") String name, @JsonProperty("url") String url, @JsonProperty("reads") long reads) {
        this.name = name;
        this.url = url;
        this.reads = reads;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getReads() {
        return reads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopArticle that = (TopArticle) o;
        return reads == that.reads &&
                Objects.equals(name, that.name) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, reads);
    }

    @Override
    public String toString() {
        return "TopArticle{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", reads=" + reads +
                '}';
    }
}
