package com.senacor.tecco.codecamp.reactive.katas;

import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;

/**
 * @author Andreas Keefer
 */
public class Kata8Batch {

    @Test
    public void withoutBatch() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableBurst, der einen Stream von WikiArtikel Namen liefert,
        // die gerade gelesen werden.
        // 2. beobachte den Stream 2 sekunden lang
        // 3. speichere die Artikel ab (WikiService.save(String)). Der service gibt die laufzeit zurück
        // 4. summiere die laufzeit der save calls und gib dies aus

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .map(WIKI_SERVICE::save)
                .reduce((l, r) -> l + r)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(10000);
    }


    @Test
    public void batch() throws Exception {
        // 1. mache das gleiche wie oben, nur verwende diesmal die #save(Iterable) Methode um einene Batch von
        //    Artikeln zu speichern. Beachte dabei, dass du hier potentiel einen Stream hast, du kannst also nicht warten
        //    bis der Stream alle artikel geliefert hat und dann alles in einem grossen batch abspeichern

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .buffer(10)
                .map(WIKI_SERVICE::save)
                .reduce((l, r) -> l + r)
                .subscribe(System.out::println, Throwable::printStackTrace);
        Thread.sleep(10000);
    }
}
