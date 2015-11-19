package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
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
        WaitMonitor monitor = new WaitMonitor();
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableBurst, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden.
        // 2. beobachte den Stream 2 sekunden lang
        // 3. speichere die Artikel ab (WikiService.save(String)). Der service gibt die laufzeit zurück
        // 4. summiere die laufzeit der save calls und gib dies aus

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst()
                .take(2, TimeUnit.SECONDS)
                .flatMap(a -> Observable.just(WIKI_SERVICE.save(a)))
                .reduce((a,b) -> a+b)
                .subscribe(out -> System.out.println("Laufzeit: "+out), e -> e.printStackTrace(), () -> monitor.complete());

        monitor.waitFor(30, TimeUnit.SECONDS);
    }


    @Test
    public void batch() throws Exception {
        // 1. mache das gleiche wie oben, nur verwende diesmal die #save(Iterable) Methode um einene Batch von
        //    Artikeln zu speichern. Beachte dabei, dass du hier potentiel einen Stream hast, du kannst also nicht warten
        //    bis der Stream alle artikel geliefert hat und dann alles in einem grossen batch abspeichern

        WIKI_SERVICE.wikiArticleBeingReadObservableBurst();
    }
}
