package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

    @Test
    public void filterObservable() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. Filtere die Name so, dass nur Artikel mit mindestens 15 Buchstaben akzeptiert werden und gib alles auf der Console aus

        WaitMonitor waitMonitor = new WaitMonitor();

        WIKI_SERVICE.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS).filter(name -> name.length() >= 15)
            .subscribe(name -> ReactiveUtil.print("wikiArticleWithAtLeast15Characters=%s", name), Throwable::printStackTrace, waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservable, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden
        // 2. Der Stream liefert uns zu viel Artikel, so schnell koennen wir die Artikel nicht bearbeiten.
        //    Beschränke den Stream auf einen Artikel alle 500ms. Direkt die Parameter am wikiArticleBeingReadObservable zu Ändern gilt natürlich nicht ;)

        WaitMonitor waitMonitor = new WaitMonitor();

        WIKI_SERVICE.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
            .filter(name -> name.length() >= 15)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(name -> ReactiveUtil.print("wikiArticleWithAtLeast15Characters=%s", name), Throwable::printStackTrace, waitMonitor::complete);;

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }
}
