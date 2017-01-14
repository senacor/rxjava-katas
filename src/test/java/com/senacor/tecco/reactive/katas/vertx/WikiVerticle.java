package com.senacor.tecco.reactive.katas.vertx;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class WikiVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(WikiVerticle.class);

    private final WikiService wikiService = new WikiService();
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Override
    public void start() throws Exception {

        vertx.eventBus().registerDefaultCodec(ParsedPage.class, new ParsedPageCodec());

        vertx.eventBus().<String>consumer("article.fetch").handler(msg -> {
            wikiService.fetchArticleObservable(msg.body())
                .subscribe(msg::reply);
        });

        vertx.eventBus().<String>consumer("article.parse").handler(msg -> {
            wikiService.parseMediaWikiTextObservable(msg.body())
                .subscribe(msg::reply);
        });

        vertx.eventBus().<ParsedPage>consumer("article.count").handler(msg -> {
            countService.countWordsObervable(msg.body())
                .subscribe(msg::reply);
        });

        vertx.eventBus().<ParsedPage>consumer("article.rate").handler(msg -> {
            ratingService.rateObservable(msg.body())
                .subscribe(msg::reply);
        });
    }
}
