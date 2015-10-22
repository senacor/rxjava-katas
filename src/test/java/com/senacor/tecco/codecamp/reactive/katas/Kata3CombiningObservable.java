package com.senacor.tecco.codecamp.reactive.katas;

import com.senacor.tecco.codecamp.reactive.WaitMonitor;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.codecamp.reactive.services.WikiService.WIKI_SERVICE;
import static rx.Observable.*;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    @Test
    public void combiningObservablewithJoin() throws Exception {
        final String articleName = "Superman";


        WIKI_SERVICE.fetchArticle(articleName)
                // Flatten Observable of Observable
                .flatMap(article -> WIKI_SERVICE.parseMediaWikiText(article))
                // flatMap weil join ein Observable zuruekcgibt
                .flatMap(parsedPage -> {
                    Observable<Integer> integerObservable = WIKI_SERVICE.countWords(parsedPage);
                    Observable<Integer> rateObs = WIKI_SERVICE.rate(parsedPage);
                    return integerObservable.join(
                            rateObs,
                            s1 -> timer(1000, TimeUnit.MILLISECONDS),
                            s2 -> timer(1000, TimeUnit.MILLISECONDS),
                            (s1, s2) -> "{articleName:" + articleName + ", rating:" + s1 + ", wordCount:" + s2 + "}");

                })
                .subscribe(
                        json -> {
                            System.out.println("json: " + json);
                        },
                        Throwable::printStackTrace,
                        () -> System.out.println("Fertig")
                );

        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // WikiService.WIKI_SERVICE.fetchArticle()
    }

    @Test
    public void combiningObservablewithZip() throws Exception {
        final String articleName = "Superman";

        final WaitMonitor monitor = new WaitMonitor();
        WIKI_SERVICE.fetchArticle(articleName)
                // Flatten Observable of Observable
                .flatMap(article -> WIKI_SERVICE.parseMediaWikiText(article))
                .flatMap(parsedPage -> zip(
                                WIKI_SERVICE.rate(parsedPage),
                                WIKI_SERVICE.countWords(parsedPage),
                                (s1, s2) -> "{articleName:" + articleName + ", rating:" + s1 + ", wordCount:" + s2 + "}")
                )
                .subscribe(
                    json -> System.out.println("json: " + json),
                    Throwable::printStackTrace,
                    monitor::complete
                );

        monitor.waitFor(5, TimeUnit.SECONDS);

        // 1. Wikiartikel holen und parsen
        // 2. Benutze jetzt den WikiService#rate() und #countWords() und kombiniere beides im JSON-Format
        //    und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

        // WikiService.WIKI_SERVICE.fetchArticle()
    }

}
