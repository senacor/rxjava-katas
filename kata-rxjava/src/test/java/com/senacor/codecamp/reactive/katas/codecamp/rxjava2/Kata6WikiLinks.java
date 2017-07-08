package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.nightmare;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;

import de.tudarmstadt.ukp.wikipedia.parser.Link;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = WikiService.create();
    
    WaitMonitor waitMonitor = new WaitMonitor();

    @Test
    @KataClassification(nightmare)
    public void linksObservable() throws Exception {
        // 1. load and parse an arbitrary wiki article
        // 2. map all internal links of an article (parsedPage.getSections().getLinks(Link.type.INTERNAL))
        //    to an observable and print it to console <Start_Article> -> <Link/Article_Name>)doOnEach
        // 3. if that works: add a recursion (only 1 step) that loads the articles for the links and print all links contained in the article
        // 4. measure the performance and optimize the performance with scheduler
        // 5. do not print a combination <Start_Artikel> -> <Link/Artikel_Name> twice
        //wikiService.fetchArticleObservable(...);
    	
    	wikiService.fetchArticleObservable("Observable")
    		.take(20)
     		.map(wikiService::parseMediaWikiText)
     		.map(parsedPage -> parsedPage.getSections())
     		.flatMapIterable(sections -> sections)
     		.map(section -> section.getLinks(Link.type.INTERNAL))
     		.flatMapIterable(links -> links)
     		.doOnEach(link -> System.out.println(link.getValue().getTarget()))
     		.subscribe(n -> System.out.println("\n"), Throwable::printStackTrace, () -> waitMonitor.complete()); 
    	
        waitMonitor.waitFor(15, TimeUnit.SECONDS);
    	
    }

}
