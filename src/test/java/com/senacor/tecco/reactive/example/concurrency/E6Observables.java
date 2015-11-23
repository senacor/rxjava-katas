package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class E6Observables {

    @Test
    public void thatPlaneInfosAreCombinedWithObservables() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        Observable.from(planeTypes)
            .flatMap(this::fetchArticle)
            .map(this::parsePlaneInfo)
            .subscribe((planeInfo)-> {
                print(planeInfo);
            });

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
