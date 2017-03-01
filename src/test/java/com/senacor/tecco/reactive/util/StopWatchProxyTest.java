package com.senacor.tecco.reactive.util;

import com.senacor.tecco.reactive.services.integration.MediaWikiTextParser;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapi;
import com.senacor.tecco.reactive.services.integration.WikipediaServiceJapiMock;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class StopWatchProxyTest {

    private final ParsedPage parsedPage = new MediaWikiTextParser().parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                    "== Religion ==\n" +
                    "\n" +
                    "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                    "== Weblinks ==\n" +
                    "\n" +
                    "{{commonscat|42}}");

    @Test
    public void newJdkProxy() throws Exception {
        WikipediaServiceJapiMock target = new WikipediaServiceJapiMock(1);
        WikipediaServiceJapi wikipediaServiceJapi = StopWatchProxy.newJdkProxy(target);
        String article = wikipediaServiceJapi.getArticle("42");
        assertThat(article, startsWith("{{Dieser Artikel|behandelt das Jahr 42"));
        assertThat(wikipediaServiceJapi, not(instanceOf(target.getClass())));
    }

    @Test
    public void proxyChaining() throws Exception {
        DummyServiceImpl target = new DummyServiceImpl();
        //DummyService dummyService = ProxyUtil.newCglibProxy(target, new StopWatchProxy(target), new StopWatchProxy(target));
        DummyService dummyService = StopWatchProxy.newJdkProxy(StopWatchProxy.newJdkProxy(target));
        int words = dummyService.countWords(parsedPage);
        assertThat("wordCount", words, is(27));
        assertThat(dummyService, not(instanceOf(target.getClass())));
    }

    @Test
    public void testToString() throws Exception {
        DummyServiceImpl target = new DummyServiceImpl();
        DummyService dummyService = StopWatchProxy.newJdkProxy(target);
        int words = dummyService.countWords(parsedPage);
        assertThat("wordCount", words, is(27));
        assertThat(dummyService.toString(), is(target.toString()));
        assertThat(dummyService.hashCode(), is(target.hashCode()));
    }
}