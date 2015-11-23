package com.senacor.tecco.reactive.katas.introduction;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

/**
 * @author Michael Menzel
 */
public class Kata3TransformObservable {

    @Test
    public void createAnObservable() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetchArticle method to transform the plane type to an Article
        // 3) use the parsePlaneInfo method to transform the article to an planeInfo object
        // 4) subscribe to the observable and print the plane type

    }


    Observable<Article> fetchArticle(String articleName) {
        return WikiService.WIKI_SERVICE_EN.fetchArticle(articleName).
                map((article) -> new Article(articleName, article));
    }


    PlaneInfo parsePlaneInfo(Article article){
        return new PlaneInfo(article.name, WikiService.WIKI_SERVICE_EN.findValue(article.content, "number built"));
    }

    class Article{
        public String name;
        public String content;

        public Article(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }

    class PlaneInfo{
        public String typeName;
        public String numberBuild;

        public PlaneInfo(String typeName, String numberBuild) {
            this.typeName = typeName;
            this.numberBuild = numberBuild;
        }

        @Override
        public String toString() {
            return "PlaneInfo{" +
                    "typeName='" + typeName + '\'' +
                    ", numberBuild='" + numberBuild + '\'' +
                    '}';
        }
    }


}
