package com.senacor.tecco.reactive.services.integration;

import com.senacor.tecco.reactive.ReactiveUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class WikipediaServiceJapiMock extends WikipediaServiceJapiImpl {

    public WikipediaServiceJapiMock() {
    }

    public WikipediaServiceJapiMock(String url) {
        this();
    }

    @Override
    protected String getPageContent(String name) throws Exception {
        Thread.sleep(1000);
        String result = readArticle(name);
        if(result == null)
        {
            throw new IllegalArgumentException("no page found with name: " + name);
        }
        return result;
    }

    public String readArticle(String name) {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("mock/" + name + ".txt").toURI());
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            print("no mockdata for article '%s' found", name);
        }
        return null;
    }

}
