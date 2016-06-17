package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Andreas Keefer
 */
public class Kata2cTransformingObservable {

    private WikiService wikiService = new WikiService();
    private CountService countService = new CountService();

    @Test
    public void transformingObservable() throws Exception {
        final WaitMonitor monitor = new WaitMonitor();
        wikiService.fetchArticleObservable("Boeing 737")
                .flatMap(article -> wikiService.parseMediaWikiTextObservable(article))
                .map(article -> countWordsWithoutA(article))
                .subscribe(next -> print("%s", next),
                        Throwable::printStackTrace,
                        () -> {
                            monitor.complete();
                            print("completed");
                            });
        monitor.waitFor(10, TimeUnit.SECONDS);
        // 1. Use the WikiService (fetchArticleObservable) and fetch an arbitrary wikipedia article
        // 2. transform the result with the WikiService#parseMediaWikiText to an object structure
        // 3. print the number of words that begin with character 'a' to the console (ParsedPage.getText())

        // wikiService.fetchArticleObservable()
    }

    private int countWordsWithoutA(ParsedPage parsedpage)
    {
        String page = parsedpage.getText();
        String[] words = page.split(" ");
        int counter = 0;
        for(String word:words){
            if(word.startsWith("a")){
                counter++;
            }
        }
        return counter;
    }

}
