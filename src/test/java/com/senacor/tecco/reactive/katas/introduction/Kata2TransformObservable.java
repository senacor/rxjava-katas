package com.senacor.tecco.reactive.katas.introduction;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

/**
 * @author Michael Menzel
 */
public class Kata2TransformObservable {

    @Test
    public void createAnObservable() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetch article method to transform the planetype to an Article
        // 3) subscribe to the observable and print the plane type

    }


    Observable<Article> fetchArticle(String articleName) {
        return WikiService.WIKI_SERVICE_EN.fetchArticle(articleName).
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
