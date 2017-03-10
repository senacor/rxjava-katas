package com.senacor.codecamp.reactive.util;

import com.senacor.codecamp.reactive.services.integration.MediaWikiTextParser;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceJapiMock;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class DelayProxyTest {

    private final ParsedPage parsedPage = new MediaWikiTextParser().parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                    "== Religion ==\n" +
                    "\n" +
                    "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                    "== Weblinks ==\n" +
                    "\n" +
                    "{{commonscat|42}}");

    @Test
    public void noDelay() throws Exception {
        DummyServiceImpl target = new DummyServiceImpl();
        DummyService dummyService = DelayProxy.newJdkProxy(target, DelayFunction.withNoDelay());
        int words = dummyService.countWords(parsedPage);
        assertThat("wordCount", words, is(27));
        assertThat(dummyService, not(instanceOf(target.getClass())));
    }

    @Test
    public void newJdkProxy() throws Exception {
        WikipediaServiceJapiMock target = new WikipediaServiceJapiMock();
        WikipediaServiceJapi wikipediaServiceJapi = DelayProxy.newJdkProxy(target, DelayFunction.staticDelay(1000));
        String article = wikipediaServiceJapi.getArticle("42");
        assertThat(article, startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
        assertThat(wikipediaServiceJapi, not(instanceOf(target.getClass())));
    }

}