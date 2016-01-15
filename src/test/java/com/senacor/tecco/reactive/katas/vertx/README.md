## Erweitere das WikiVerticle, dass den Wikiservice benutzt, um folgende Methoden 'parseMediaWikiText', 'rate' und 'countWords'.
Orientiere dich an den Beispielen aus dem Package 'com.senacor.tecco.reactive.vertx'

## Versuche Das Beispiel aus Kata3CombiningObservable abzubilden, aber nicht mit der rxjava Unterstützung von vertx, also mit Callbacks:
1. Wikiartikel holen und parsen mittels WikiVerticle
2. Benutze jetzt #rate() und #countWords() aus dem WikiVerticle und kombiniere beides im JSON-Format
     und gib das JSON auf der Console aus. Beispiel {"articleName": "Superman", "rating": 3, "wordCount": 452}
   
## verwende jetzt anstatt der Callbacks die rx Vertex Klassen 'io.vertx.lang.rxjava.AbstractVerticle'
