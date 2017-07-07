package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.katas.KataClassification;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.nightmare;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = WikiService.create();

    @Test
    @KataClassification(nightmare)
    public void linksObservable() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion (only 1 step) that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice

        final String articleName = "Vaporware";

        getRelationsFromArticle(articleName)
                .doOnNext(System.out::println)
                .map(Relation::getTarget)
                .flatMap(a -> getRelationsFromArticle(a).subscribeOn(Schedulers.io()))
                .doOnNext(System.out::println)
                .test()
                .awaitDone(10, TimeUnit.SECONDS);
    }

    private Observable<Relation> getRelationsFromArticle(String srcArticle) {
        return wikiService.fetchArticleObservable(srcArticle)
                .map(wikiService::parseMediaWikiText)
                .flatMap(page -> Observable.fromIterable(page.getSections()))
                .flatMap(section -> Observable.fromIterable(section.getLinks(Link.type.INTERNAL)))
                .map(Link::getTarget)
                .distinct()
                .map(target -> new Relation(srcArticle, target));
    }

    private static class Relation {
        String source;
        String target;

        public Relation(String src, String target) {
            this.source = src;
            this.target = target;
        }

        @Override
        public String toString() {
            return String.format("%s -> %s", getSource(), getTarget());
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }
    }

}
