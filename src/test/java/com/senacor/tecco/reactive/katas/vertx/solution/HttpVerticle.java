package com.senacor.tecco.reactive.katas.vertx.solution;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class HttpVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(event -> {
            String articleName = event.getParam("articleName");
            print("articleName=%s", articleName);
            vertx.eventBus().<String>sendObservable("fetchArticle", articleName)
                    .flatMap(article -> vertx.eventBus().<ParsedPage>sendObservable("parseMediaWikiText", article.body()))
                    .map(Message::body)
                    .flatMap(parsedPage -> {
                        Observable<Integer> countWords = vertx.eventBus().<Integer>sendObservable("countWords", parsedPage)
                                .map(Message::body);
                        Observable<Integer> rate = vertx.eventBus().<Integer>sendObservable("rate", parsedPage)
                                .map(Message::body);
                        return Observable.zip(countWords, rate, (count, rateing) -> "count=" + count + " rate=" + rateing);
                    })
                    .subscribe(res -> {
                        print(res);
                        event.response().headers().add("Content-Type", "text/plain");
                        event.response().end(res);
                    }, error -> {
                        event.response().setStatusCode(404);
                        event.response().end();
                    });
        }).listen(8081);
    }
}
