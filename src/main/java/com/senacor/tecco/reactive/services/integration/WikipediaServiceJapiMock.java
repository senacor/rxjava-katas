package com.senacor.tecco.reactive.services.integration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class WikipediaServiceJapiMock extends WikipediaServiceJapiImpl {

    public WikipediaServiceJapiMock() {
    }

    @Override
    protected String getPageContent(String name) throws Exception {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("mock/" + name + ".txt").toURI());
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            print("no mockdata for article '%s' found", name);
        }
        return null;
    }

}
