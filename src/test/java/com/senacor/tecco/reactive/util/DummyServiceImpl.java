package com.senacor.tecco.reactive.util;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

import java.util.StringTokenizer;

/**
 * @author Andreas Keefer
 */
public class DummyServiceImpl implements DummyService {
    @Override
    public int countWords(final ParsedPage parsedPage) {
        return new StringTokenizer(parsedPage.getText(), " ").countTokens();
    }
}
