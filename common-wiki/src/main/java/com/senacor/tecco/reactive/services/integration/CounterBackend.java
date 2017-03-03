package com.senacor.tecco.reactive.services.integration;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

/**
 * @author Andreas Keefer
 */
public interface CounterBackend {
    int countWords(ParsedPage parsedPage);
}
