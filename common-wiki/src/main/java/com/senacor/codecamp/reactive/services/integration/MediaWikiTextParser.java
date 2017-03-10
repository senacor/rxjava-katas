package com.senacor.codecamp.reactive.services.integration;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

/**
 * @author Andreas Keefer
 */
public class MediaWikiTextParser {

    private final MediaWikiParserFactory pf = new MediaWikiParserFactory();
    private final MediaWikiParser parser = pf.createParser();

    public ParsedPage parse(String mediaWikiText) {
        ParsedPage res = parser.parse(mediaWikiText);
        if (res == null) {
            throw new IllegalArgumentException("text not parseable: \"" + mediaWikiText + '"');
        }

        return res;
    }
}
