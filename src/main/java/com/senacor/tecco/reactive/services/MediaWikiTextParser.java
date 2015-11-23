package com.senacor.tecco.reactive.services;

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

    private MediaWikiParserFactory pf = new MediaWikiParserFactory();
    private MediaWikiParser parser = pf.createParser();

    public static void main(String[] args) {
        ParsedPage parsedPage = new MediaWikiTextParser().parser.parse(
                "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                        "== Religion ==\n" +
                        "\n" +
                        "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                        "== Weblinks ==\n" +
                        "\n" +
                        "{{commonscat|42}}");
        for (Link link : parsedPage.getLinks()) {
            System.out.println("link=" + link.getTarget());
        }
        for (Section section : parsedPage.getSections()) {
            System.out.println("Section: " + section.getTitle());
            for (Link link : section.getLinks(Link.type.INTERNAL)) {
                System.out.println(" sectionLink=" + link.getTarget());
            }
        }
    }

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
