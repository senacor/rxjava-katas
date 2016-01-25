package com.senacor.tecco.reactive.katas.introduction.solution;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class Kata2TransformObservable {

    private final WikiService wikiService = new WikiService("en");

    @Test
    public void createAnObservable() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetch article method to transform the plane type to an Article
        // 3) subscribe to the observable and print the article content

        Observable.from(planeTypes)
                .flatMap(planeType -> fetchArticle(planeType))
                .subscribe(article -> print("next: %s", article.content),
                        Throwable::printStackTrace,
                        () -> print("complete!"));


    }


    /**
     * fetches an article from the wikipedia
     * @param articleName name of the wikipedia article
     * @return an article
     */
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName).
                map((article) -> new Article(articleName, article));
    }

    class Article{
        public String name;
        public String content;

        public Article(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }

}
