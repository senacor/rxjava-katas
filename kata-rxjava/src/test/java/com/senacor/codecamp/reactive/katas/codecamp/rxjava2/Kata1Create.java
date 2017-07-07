package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.integration.WikipediaServiceMediaWikiBot;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Test;

import io.reactivex.Observable;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;

/**
 * @author Andreas Keefer
 */
public class Kata1Create {

    @Test
    @KataClassification(mandatory)
    public void createAnObservable() throws Exception {
        final String articleName = "Observable";
        // Create an observable from getArticle
        Observable.create(subscriber -> {try {
            subscriber.onNext(getArticle(articleName).getText());
        } catch (Exception e) {
            subscriber.onError(e);
        }
        })
                .subscribe(next -> ReactiveUtil.print("beginning: %s", next),
                        (throwable) -> throwable.printStackTrace(),
                        () -> {
                            ReactiveUtil.print("end article");
                        }


        );

    }

    public Article getArticle(String name) {
        return new WikipediaServiceMediaWikiBot().getArticle(name);
    }
}
