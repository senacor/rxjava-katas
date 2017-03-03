package com.senacor.tecco.reactive.katas.codecamp.reactor.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.katas.codecamp.rxjava2.solution.Kata6WikiLinks.WikiLink;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private static Scheduler ioScheduler = Schedulers.elastic();
    private static Scheduler computation = Schedulers.parallel();
    private final WikiService wikiService = WikiService.create();

    @Test
    public void links() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an Flux and print it to console <Start_Article> -> <Link/Article_Name>)
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
}