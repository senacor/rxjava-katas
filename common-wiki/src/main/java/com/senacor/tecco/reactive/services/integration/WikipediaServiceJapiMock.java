package com.senacor.tecco.reactive.services.integration;

import org.apache.commons.io.IOUtils;
import reactor.core.publisher.Mono;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class WikipediaServiceJapiMock extends WikipediaServiceJapiImpl {

    public WikipediaServiceJapiMock() {
    }

    @Override
    protected String getPageContent(String name) throws Exception {
        try {
            return IOUtils.toString(this.getClass().getResourceAsStream("/mock/" + name + ".txt"),
                    "UTF-8");
        } catch (Exception e) {
            print("no mockdata for article '%s' found", name);
        }
        return null;
    }

    @Override
    public Mono<String> getArticleNonBlocking(String name) {
        return Mono.just(name)
                .map(this::getArticle);
    }
}
