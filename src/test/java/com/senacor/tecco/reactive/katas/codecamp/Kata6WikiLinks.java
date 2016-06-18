package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = new WikiService();

    class WikiSuperDuperLinks {
        private String name;
        private String target;

        public WikiSuperDuperLinks(String name, String target) {
            this.name = name;
            this.target = target;
        }

        public String getName() {
            return name;
        }

        public String getTarget() {
            return target;
        }

        @Override
        public String toString() {
            return String.format("<%s> -> <%s>", name, target);
        }
    }


    public Observable<WikiSuperDuperLinks> getAllLinks(String articleName){
        return wikiService.fetchArticleObservable(articleName).subscribeOn(Schedulers.io())
                .map(wikiService::parseMediaWikiText)
                .flatMap(parsedPage -> getLinks(parsedPage.getSections()))
                .distinct()
                .map(link -> new WikiSuperDuperLinks(articleName, link.getTarget())).subscribeOn(Schedulers.io());

    }

    @Test
    public void linksObservable() throws Exception {
        WaitMonitor monitor = new WaitMonitor();
        String article = "777";
        Observer<WikiSuperDuperLinks> observer = Observers.create(test -> getAllLinks(test.getTarget()));
        Observer printingObserver = Observers.create(test -> print(test.toString()));
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        getAllLinks(article)
                .doOnEach(printingObserver)
                .flatMap(linking -> getAllLinks(((WikiSuperDuperLinks)linking).getTarget()))

        // 3. if that works: add a recursion that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        //wikiService.fetchArticleObservable(...);
                .subscribe(next -> print(next));
        monitor.waitFor(15, TimeUnit.SECONDS);
    }

    private Observable<Link> getLinks(List<Section> sectionList){

        List<Link> linkList = new ArrayList<>();
        sectionList.forEach(section -> {
            linkList.addAll(section.getLinks(Link.type.INTERNAL));
        });
        return Observable.from(linkList);
    }

}
