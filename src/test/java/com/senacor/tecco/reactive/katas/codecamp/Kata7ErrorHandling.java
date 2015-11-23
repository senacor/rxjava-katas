package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    @Test
    public void errors() throws Exception {
        // 1. Benutze den WikiService#wikiArticleBeingReadObservableWithRandomErrors, der einen Stream von WikiArtikel Namen liefert, die gerade gelesen werden.
        // 2. Filtere zuerst die Bursts heraus.
        // 3. Behandle auftretende Fehler: Versuche zuerst einen paar Retrys mit steigender pause dazwischen
        // 4. Falls die Retrys nicht helfen beende den Stream mit einem Default

        WikiService.WIKI_SERVICE.wikiArticleBeingReadObservableWithRandomErrors();
    }

}
