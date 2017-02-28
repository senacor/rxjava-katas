package com.senacor.tecco.reactive.services.integration;

import com.bitplan.mediawiki.japi.Mediawiki;

import static com.senacor.tecco.reactive.ReactiveUtil.getThreadId;

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
        String pageContent = wiki.getPageContent(name);
        if (pageContent == null) {
            throw new IllegalArgumentException("no page found with name: " + name);
        }
        return pageContent;
    }

    @Override
    public String getArticle(String name) {
        try {
            final long start = System.currentTimeMillis();
            String res = getPageContent(name);
            System.out.println(getThreadId() +
                               "profiling getArticle(" + name + "): " + (System.currentTimeMillis() - start) + "ms");
            return res;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
