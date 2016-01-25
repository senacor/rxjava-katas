package com.senacor.tecco.reactive.services.integration;

import com.bitplan.mediawiki.japi.Mediawiki;
import rx.Observable;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;

/**
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 *
 */
public class WikipediaServiceJapiMock extends WikipediaServiceJapiImpl {

    Map<String, String> articles = new HashMap<>();

    public WikipediaServiceJapiMock() {
        setUpArticelMap();
    }

    public WikipediaServiceJapiMock(String url) {
        setUpArticelMap();
    }

    @Override
    protected String getPageContent(String name) throws Exception {
        return articles.get(name);
    }

    public void setUpArticelMap() {
        articles.put("Boeing 747", readArticle("747"));
        articles.put("Boeing 777", readArticle("777"));
    }

    public String readArticle(String name){
        try {
            Path path =  Paths.get(ClassLoader.getSystemResource(name + ".txt").toURI());
            return new String(Files.readAllBytes(path));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
