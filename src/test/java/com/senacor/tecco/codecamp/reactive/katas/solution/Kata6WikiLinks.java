package com.senacor.tecco.codecamp.reactive.katas.solution;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.newCachedThreadPoolScheduler;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.newScheduler;
import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private static Scheduler scheduler = newScheduler(25, "io");
    private static Scheduler cachedScheduler = newCachedThreadPoolScheduler("cached");

    @Test
    public void linksObservable() throws Exception {
        // 1. lade und parse einen beliebigen Wiki Artikel
        // 2. mappe alle internen Links (parsedPage.getSections().getLinks(Link.type.INTERNAL)) des Artikel auf ein Observable und gib dies auf der Console aus (<Start_Artikel> -> <Link/Artikel_Name>)
        // 3. Wenn das Funktioniert füge eine Rekursion hinzu und lade alle Artikel zu den Links und gib wiederum alle Links auf der Console aus
        // 4. miss die Performance und optimiere die Performance mit Schedulern
        // 5. gib keine Kombination <Start_Artikel> -> <Link/Artikel_Name> mehrfach aus

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

    private static Observable<WikiLink> getLinks(final String wikiArticle) {
        //print("getLinks fuer Artikel: %s", wikiArticle);
        return WIKI_SERVICE.fetchArticle((wikiArticle))
                .subscribeOn(scheduler)
                .flatMap(WIKI_SERVICE::parseMediaWikiText)
                .subscribeOn(Schedulers.computation())
                .filter(parsedPage -> parsedPage != null)
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
