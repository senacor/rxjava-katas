package com.senacor.tecco.reactive.services.integration;

import com.bitplan.mediawiki.japi.Mediawiki;

/**
 * @author Andreas Keefer
 */
public class WikipediaServiceJapiImpl implements WikipediaServiceJapi {

    private final Mediawiki wiki;

    public WikipediaServiceJapiImpl() {
        this("https://de.wikipedia.org");
    }

    public WikipediaServiceJapiImpl(String url) {
        try {
            wiki = new Mediawiki(url);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
}
