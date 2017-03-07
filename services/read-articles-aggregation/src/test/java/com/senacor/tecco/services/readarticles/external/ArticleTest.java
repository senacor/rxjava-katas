package com.senacor.tecco.services.readarticles.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Daniel Heinrich on 07/03/2017.
 */
public class ArticleTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldDeserialize() throws IOException {
        String json = "{\"name\": \"boing\", \"content\": \"some text content\"}";
        Article article = objectMapper.readValue(json, Article.class);
        assertEquals("boing", article.getName());
        assertEquals("some text content", article.getContent());
    }
}