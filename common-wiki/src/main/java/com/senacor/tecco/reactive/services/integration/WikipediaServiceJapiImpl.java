package com.senacor.tecco.reactive.services.integration;

import com.bitplan.mediawiki.japi.Mediawiki;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientResponse;

import java.io.IOException;

/**
 * @author Andreas Keefer
 */
public class WikipediaServiceJapiImpl implements WikipediaServiceJapi {

    private final Mediawiki wiki;
    private final HttpClient client;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public WikipediaServiceJapiImpl() {
        this("https://de.wikipedia.org");
    }

    public WikipediaServiceJapiImpl(String url) {
        try {
            wiki = new Mediawiki(url);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        this.client = HttpClient.create();
    }

    protected String getPageContent(String name) throws Exception {
        return wiki.getPageContent(name);
    }

    @Override
    public String getArticle(String name) {
        String pageContent;
        try {
            pageContent = getPageContent(name);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        if (pageContent == null) {
            throw new ArticleNotFoundException("no page found with name: " + name);
        }
        return pageContent;
    }

    @Override
    public Mono<String> getArticleNonBlocking(String name) {
        return Mono.just(name)
                .map(this::buildQueryUrl)
                .flatMap(this.client::get)
                .flatMap(this::httpResponse2String)
                .reduce(String::concat)
                .map(this::parseJsonContend);

    }

    private String buildQueryUrl(String articleName) {
        final String normalizedTitle;
        try {
            normalizedTitle = wiki.normalizeTitle(articleName);
        } catch (Exception e) {
            throw new ArticleNotFoundException("Articl name '" + articleName + "' could not be parsed", e);
        }
        final String queryParams = ("&prop=revisions&rvprop=content&titles=" + normalizedTitle).replace("|", "%7C");
        final String queryUrl = wiki.getSiteurl() + wiki.getScriptPath() + "/api.php?&action=query"
                + "&format=json" + queryParams;
        return queryUrl;
    }

    private String parseJsonContend(String json) {
        try {
            JsonNode contentNode = OBJECT_MAPPER.readTree(json).findValue("*");
            return contentNode.asText();
        } catch (IOException e) {
            throw new ArticleNotFoundException("parsing error", e);
        }
    }

    private Publisher<String> httpResponse2String(HttpClientResponse response) {
        if (!HttpResponseStatus.OK.equals(response.status())) {
            throw new ArticleNotFoundException("HTTP status not OK: " + response.status());
        }
        return response.receive().asString()
                .limitRate(1);
    }
}
