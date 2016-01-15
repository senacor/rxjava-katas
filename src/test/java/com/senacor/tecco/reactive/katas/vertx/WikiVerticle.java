package com.senacor.tecco.reactive.katas.vertx;

import com.senacor.tecco.reactive.services.WikiService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Andreas Keefer
 */
public class WikiVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(WikiVerticle.class);

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("fetchArticle").handler(msg -> {
            new WikiService().fetchArticleObservable(msg.body().toString())
                    .subscribe(msg::reply);
        });
    }
}
