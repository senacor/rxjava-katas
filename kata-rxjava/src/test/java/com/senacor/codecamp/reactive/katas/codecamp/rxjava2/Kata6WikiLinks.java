package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.WaitMonitor;

import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import com.senacor.codecamp.reactive.katas.KataClassification;
import org.junit.Test;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.nightmare;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata6WikiLinks {

    private final WikiService wikiService = WikiService.create();
    private final String articleName = "Observable";

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
    	
    	WaitMonitor monitor = new WaitMonitor();

        linksRec(articleName, 1)	
    	.subscribe(
    			next -> System.out.println("subscribed: " + next),
    			Throwable::printStackTrace,
    			() -> monitor.complete()
    			);
        
        monitor.waitFor(10, TimeUnit.SECONDS);
       
    }
    
    public Observable<String> linksRec(String articleName, int depth) {
    	return Observable.just(articleName)
    	.flatMap(aName -> 
    		Observable.just(aName)
    		.flatMap((name) -> wikiService.fetchArticleObservable(name).subscribeOn(Schedulers.io()))
        	.map(wikiService::parseMediaWikiText)
        	.flatMap((text) -> {
        		List<Link> links = new LinkedList<Link>();
        		
        		for(Section section: text.getSections()) {
        			links.addAll(section.getLinks(Link.type.INTERNAL));
        		}
        		
        		return Observable
        				.fromIterable(links)
        				.distinct()
        				.flatMap(link -> {
		        			if (depth > 0) {
		        				return linksRec(link.getTarget(), depth-1);
		        			}
		        			return Observable.just(link.getTarget());
		        		})
        				.distinct()
        				.map(link -> String.format("%s -> %s", aName, link));
        	})
    	);
	}

}
