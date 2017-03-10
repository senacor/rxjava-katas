package com.senacor.codecamp.reactive.services.integration;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.apache.commons.lang3.Validate;

import java.util.StringTokenizer;

import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class CounterBackendImpl implements CounterBackend {

    @Override
    public int countWords(final ParsedPage parsedPage) {
        Validate.notNull(parsedPage, "parsedPage must not be null");
        String text = parsedPage.getText();
        int wordCount = new StringTokenizer(text, " ").countTokens();
        print("countWords: %s", wordCount);
        return wordCount;
    }
}
