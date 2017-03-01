package com.senacor.tecco.reactive.util;

import com.senacor.tecco.reactive.services.integration.MediaWikiTextParser;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Andreas Keefer
 */
public class DefaultProxyBehaviorTest {

    private final ParsedPage parsedPage = new MediaWikiTextParser().parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                    "== Religion ==\n" +
                    "\n" +
                    "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                    "== Weblinks ==\n" +
                    "\n" +
                    "{{commonscat|42}}");

    @Test
    public void proxyChaining() throws Exception {
        DummyServiceImpl target = new DummyServiceImpl();

        DummyService dummyService = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(target, FlakinessFunction.noFlakiness())
                        , DelayFunction.withNoDelay()));

        int words = dummyService.countWords(parsedPage);
        assertThat("wordCount", words, is(27));
        assertThat(dummyService, not(instanceOf(target.getClass())));
        assertThat(dummyService, not(sameInstance(target.getClass())));
    }
}