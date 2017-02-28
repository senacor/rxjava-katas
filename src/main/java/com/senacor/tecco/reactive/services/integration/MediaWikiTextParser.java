package com.senacor.tecco.reactive.services.integration;

import com.senacor.tecco.reactive.ReactiveUtil;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.text.ParseException;
import java.util.Optional;

/**
 * @author Andreas Keefer
 */
public class MediaWikiTextParser {

    private final MediaWikiParserFactory pf = new MediaWikiParserFactory();
    private final MediaWikiParser parser = pf.createParser();

    public ParsedPage parse(String mediaWikiText) {
        final long start = System.currentTimeMillis();
        ParsedPage res = parser.parse(mediaWikiText);
        if (res == null) {
            throw new IllegalArgumentException("text not parseable: \"" + mediaWikiText + "\"");
        }

        System.out.println(ReactiveUtil.getThreadId() + "profiling parse("
                           //+ res.getName()
                           //+ mediaWikiText
                           + "): " + (System.currentTimeMillis() - start) + "ms");
        return res;
    }
}
