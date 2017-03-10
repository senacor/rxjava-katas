package com.senacor.codecamp.reactive.util;

import com.senacor.codecamp.reactive.services.integration.MediaWikiTextParser;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    @Test
    public void excludeMethodNmaes() throws Exception {
        DummyService dummyService = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new DummyServiceImpl(), FlakinessFunction.alwaysFail())
                        , DelayFunction.withNoDelay()));
        assertThat(dummyService.equals("asd"), is(false));
        assertThat(dummyService.hashCode(), notNullValue());
        assertThat(dummyService.toString(), startsWith(DummyServiceImpl.class.getName()));
        try {
            dummyService.countWords(parsedPage);
            fail("Exeption Expected");
        } catch (UncheckedIOException e) {
            assertThat(e.getCause(), instanceOf(IOException.class));
        }
    }

    @Test
    public void publisherTestError() throws Exception {
        DummyService dummyService = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new DummyServiceImpl(), FlakinessFunction.alwaysFail())
                        , DelayFunction.withNoDelay()));

        Mono<String> a = dummyService.mono("a");

        StepVerifier.create(a)
                .expectNextCount(0)
                .expectError(UncheckedIOException.class)
                .verify();
    }

    @Test
    public void publisherTestOK() throws Exception {
        DummyService dummyService = StopWatchProxy.newJdkProxy(
                DelayProxy.newJdkProxy(
                        FlakyProxy.newJdkProxy(new DummyServiceImpl(), FlakinessFunction.noFlakiness())
                        , DelayFunction.staticDelay(500)));

        Mono<String> a = dummyService.mono("a");

        Duration duration = StepVerifier.create(a)
                .expectNext("a")
                .verifyComplete();
        int millis = duration.getNano() / 1000000;
        assertThat("duration in millis", millis, greaterThanOrEqualTo(500));
    }
}