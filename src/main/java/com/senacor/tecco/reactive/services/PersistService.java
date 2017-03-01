package com.senacor.tecco.reactive.services;

/**
 * @author Andreas Keefer
 */
public interface PersistService {
    long save(String wikiArticle);

    long save(Iterable<String> wikiArticle);
}
