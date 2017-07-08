package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.util.WaitMonitor;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.nightmare;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(nightmare)
    public void linksObservable() throws Exception {

        WaitMonitor waitMonitor = new WaitMonitor();
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion (only 1 step) that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        String articleName = "Schnitzel";
        getAllInternalLinks(articleName)
                .flatMap((link) -> getAllInternalLinks(link.getTarget())
                        .map(newLink -> String.format("%s -> %s", link.getText(), newLink.getText()))
                        .subscribeOn(Schedulers.io())
                )
                .distinct()
                .subscribeOn(Schedulers.io())
                .subscribe(System.out::println,
                        Throwable::printStackTrace,
                        waitMonitor::complete);
        waitMonitor.waitFor(100, TimeUnit.SECONDS);
    }

    private Observable<Link> getAllInternalLinks(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map(wikiService::parseMediaWikiText)
                .flatMap(page -> Observable.fromIterable(page.getSections()))
                .flatMap(section -> Observable.fromIterable(section.getLinks(Link.type.INTERNAL)));
    }

}
