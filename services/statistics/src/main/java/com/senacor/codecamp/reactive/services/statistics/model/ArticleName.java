package com.senacor.codecamp.reactive.services.statistics.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Andreas Keefer
 */
public class ArticleName implements Serializable {

    private final String name;

    @JsonCreator
    public ArticleName(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ArticleName other = (ArticleName) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "ArticleName{" +
                "name='" + name + '\'' +
                '}';
    }
}
