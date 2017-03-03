package com.senacor.tecco.reactive.katas.introduction;

import com.senacor.tecco.reactive.services.WikiService;
import io.reactivex.Observable;
import org.junit.Test;

import static com.senacor.tecco.reactive.util.ReactiveUtil.findValue;

/**
 * @author Dr. Michael Menzel
 */
public class Kata3TransformObservable {
    private final WikiService wikiService = WikiService.create("en");

    @Test
    public void createAnObservable() throws Exception {
        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        // 1) create an observable that emits the plane type
        // 2) use the fetchArticleObservable method to transform the plane type to an Article
        // 3) use the parsePlaneInfo method to transform the article to an planeInfo object
        // 4) subscribe to the observable and print the plane information

    }

    /**
     * fetches an article from the wikipedia
     *
     * @param articleName name of the wikipedia article
     * @return an article
     */
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map((article) -> new Article(articleName, article));
    }

    /**
     * Extracts plane-related information from an wikipedia article
     *
     * @param article wikipedia article
     * @return plane information
     */
    PlaneInfo parsePlaneInfo(Article article) {
        return new PlaneInfo(article.name, findValue(article.content, "number built"));
    }

    static class Article {
        public String name;
        public String content;

        public Article(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }

    static class PlaneInfo {
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
