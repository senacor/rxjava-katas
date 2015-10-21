package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.ReactiveUtil.print;
import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

 //   @Test
    public void filterObservable() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable,
        // der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. Filtere die Name so, dass nur Artikel mit mindestens 15 Buchstaben akzeptiert werden
        // und gib alles auf der Console aus

        final WaitMonitor monitor = new WaitMonitor();
        WIKI_SERVICE.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .filter(s -> s.length() > 14)
                .subscribe(article -> print("Result: " + article),
                        error -> {},
                        monitor::complete
                );
        monitor.waitFor(1, TimeUnit.SECONDS);
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable,
        // der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. Der Stream liefert uns zu viel Artikel, so schnell koennen wir die Artikel nicht bearbeiten.
        //    Beschränke den Stream auf einen Artikel alle 500ms.
        // Direkt die Parameter am wikiArticleBeingReadObservable zu Ändern gilt natürlich nicht ;)

        final WaitMonitor monitor = new WaitMonitor();
        WIKI_SERVICE.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .sample(500, TimeUnit.MILLISECONDS)
                .subscribe(article -> print("Result: " + article),
                        error -> error.printStackTrace(),
                        monitor::complete
                );
        monitor.waitFor(6, TimeUnit.SECONDS);
    }
}
