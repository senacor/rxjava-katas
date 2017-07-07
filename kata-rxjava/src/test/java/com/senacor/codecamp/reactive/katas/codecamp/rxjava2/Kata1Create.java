package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import io.reactivex.Observable;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle

        /*Observable.create(subscriber -> {
            try {
                subscriber.onNext(getArticle(articleName).getTitle());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        })*/
        /*Observable.fromCallable(() -> getArticle(articleName).getTitle())*/
        Observable.defer(() -> Observable.just(getArticle(articleName).getTitle()))
                .test()
                .awaitDone(1, SECONDS)
                .assertValue("Observable")
                .assertComplete();

    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
