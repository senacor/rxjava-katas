package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private static Scheduler ioScheduler = Schedulers.newParallel("io", 25);
    private static Scheduler computation = Schedulers.newElastic("computation");
    private final WikiService wikiService = WikiService.create();

    @Test
    public void linksObservable() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)
        // 3. if that works: add a recursion/iteration that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice

        final WaitMonitor monitor = new WaitMonitor();

        getLinks("Observable")
                .flatMap(wikiLink -> getLinks(wikiLink.getTargetArticle()))
                .subscribe(
                        next -> {
                        },
                        error -> {
                            System.err.println(error.getMessage());
                            error.printStackTrace();
                        },
                        monitor::complete);

        monitor.waitFor(30, TimeUnit.SECONDS);
    }

    private Flux<WikiLink> getLinks(final String wikiArticle) {
        //print("getLinks fuer Artikel: %s", wikiArticle);
        return wikiService.fetchArticleFlux(wikiArticle)
                          .subscribeOn(ioScheduler)
                          .flatMap((mediaWikiText) -> Flux.from(wikiService.parseMediaWikiTextFlux(mediaWikiText))
                                                          .subscribeOn(computation))
                          .flatMapIterable(ParsedPage::getSections)
                          .flatMapIterable(section -> section.getLinks(Link.type.INTERNAL))
                          .map(link -> new WikiLink(wikiArticle, link.getTarget()))
                          .distinct()
                          .doOnNext(wikiLink -> print(wikiLink));
    }

    private static class WikiLink {
        private final String sourceArticle;
        private final String targetArticle;

        public WikiLink(String sourceArticle, String targetArticle) {
            notNull(sourceArticle, "sourceArticle must not be null");
            notNull(targetArticle, "targetArticle must not be null");
            this.sourceArticle = sourceArticle;
            this.targetArticle = targetArticle;
        }

        public String getSourceArticle() {
            return sourceArticle;
        }

        public String getTargetArticle() {
            return targetArticle;
        }

        @Override
        public String toString() {
            return sourceArticle + " -> " + targetArticle;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceArticle, targetArticle);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final WikiLink other = (WikiLink) obj;
            return Objects.equals(this.sourceArticle, other.sourceArticle)
                   && Objects.equals(this.targetArticle, other.targetArticle);
        }
    }
}