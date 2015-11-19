package com.senacor.tecco.codecamp.reactive.katas;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import com.senacor.tecco.codecamp.reactive.services.WikiService;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    @Test
    public void withoutBatch() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableBurst, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden.
        // 2. beobachte den Stream 2 sekunden lang
        // 3. speichere die Artikel ab (WikiService.save(String)). Der service gibt die laufzeit zurück
        // 4. summiere die laufzeit der save calls und gib dies aus

        WaitMonitor waitMonitor = new WaitMonitor();
        WikiService wikiService = new WikiService();
        Sum sum = new Sum();

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst() //
            .take(2, TimeUnit.SECONDS) //
            .map(wikiService::save) //
            .subscribe(laufzeit -> {
                sum.add(laufzeit);
                ReactiveUtil.print("Laufzeit: %d ms", laufzeit);
            }, Throwable::printStackTrace, waitMonitor::complete);

        waitMonitor.waitFor(3, TimeUnit.SECONDS);
        ReactiveUtil.print("Gesamtlaufzeit: %d ms", sum.sum);
    }

    @Test
    public void batch() throws Exception {
        // 1. mache das gleiche wie oben, nur verwende diesmal die #save(Iterable) Methode um einene Batch von
        //    Artikeln zu speichern. Beachte dabei, dass du hier potentiel einen Stream hast, du kannst also nicht warten
        //    bis der Stream alle artikel geliefert hat und dann alles in einem grossen batch abspeichern

        WaitMonitor waitMonitor = new WaitMonitor();
        WikiService wikiService = new WikiService();

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst() //
            .take(2, TimeUnit.SECONDS) //
            .buffer(500, TimeUnit.MILLISECONDS)
            .map(wikiService::save) //
            .reduce((l, r) ->  l + r)
            .subscribe(laufzeit -> {
                ReactiveUtil.print("Gesamtlaufzeit: %d ms", laufzeit);
            }, Throwable::printStackTrace, waitMonitor::complete);

        waitMonitor.waitFor(3, TimeUnit.SECONDS);
    }

    private class Sum {
        private long sum = 0L;

        void add(long summand) {
            sum += summand;
        }
    }
}
