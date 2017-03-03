package com.senacor.tecco.reactive.katas.vertx.solution;

import com.senacor.tecco.reactive.vertx.RxVertx;
import io.reactivex.Observable;
import io.vertx.core.AbstractVerticle;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class HttpVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(event -> {
            String articleName = event.getParam("articleName");
            print("articleName=%s", articleName);
            RxVertx.<String>send(vertx, "fetchArticle", articleName)
                    .flatMap(article -> RxVertx.send(vertx, "parseMediaWikiText", article))
                    .flatMap(parsedPage -> {
                        Observable<Integer> countWords = RxVertx.send(vertx, "countWords", parsedPage);
                        Observable<Integer> rate = RxVertx.send(vertx, "rate", parsedPage);
                        return Observable.zip(countWords, rate, (count, rating) -> "count=" + count + " rate=" + rating);
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
