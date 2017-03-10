package com.senacor.codecamp.reactive.services.integration;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import org.apache.commons.io.IOUtils;
import reactor.core.publisher.Mono;

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
            ReactiveUtil.print("no mockdata for article '%s' found", name);
        }
        return null;
    }

    @Override
    public Mono<String> getArticleNonBlocking(String name) {
        return Mono.just(name)
                .map(this::getArticle);
    }
}
