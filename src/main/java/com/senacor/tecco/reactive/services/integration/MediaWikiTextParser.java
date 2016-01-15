package com.senacor.tecco.reactive.services.integration;

import com.senacor.tecco.reactive.ReactiveUtil;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class MediaWikiTextParser {

    private final MediaWikiParserFactory pf = new MediaWikiParserFactory();
    private final MediaWikiParser parser = pf.createParser();

    public ParsedPage parse(String mediaWikiText) {
        final long start = System.currentTimeMillis();
        ParsedPage res = parser.parse(mediaWikiText);
        System.out.println(ReactiveUtil.getThreadId() + "profiling parse("
                //+ res.getName()
                //+ mediaWikiText
                + "): " + (System.currentTimeMillis() - start) + "ms");
        return res;
    }

    public Observable<ParsedPage> parseObservable(final String mediaWikiText) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(parse(mediaWikiText));
                subscriber.onCompleted();
            } catch (RuntimeException e) {
                subscriber.onError(e);
            }
        });
    }
}
