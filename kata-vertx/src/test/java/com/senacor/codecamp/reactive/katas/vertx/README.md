## Erweitere das WikiVerticle um folgende Methoden 'parseMediaWikiText', 'rate' und 'countWords'.
Verwende dazu wie bisher den WikiService, RatingService und CountService.
Orientiere dich an den Beispielen aus dem Package 'com.senacor.reactive.vertx'

## Versuche Das Beispiel aus Kata3CombiningObservable abzubilden, aber nicht mit der rxjava Unterstützung von vertx, also mit Callbacks (WikiVerticleTest):
1. Wikiartikel holen und parsen mittels WikiVerticle
2. Benutze jetzt #rate() und #countWords() aus dem WikiVerticle und kombiniere beides im JSON-Format
     und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}

## verwende jetzt anstatt der Callbacks die rx Vertx Klassen (WikiVerticleRxTest)
