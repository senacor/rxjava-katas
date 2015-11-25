package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Observable;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Retrieves and combines plane information with streams
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E5Streams {

    @Test
    public void thatPlaneInfosAreCombinedWithStreams() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747"};

        Arrays.stream(planeTypes)
                .map(this::fetchArticle)
                .map(this::parsePlaneInfo)
                .forEach((planeInfo)-> {
                    print(planeInfo);
                });
    }


    /**
     * fetches an article from the wikipedia
     * @param articleName name of the wikipedia article
     * @return an article
     */
    Article fetchArticle(String articleName) {
        return new Article(articleName, WikiService.WIKI_SERVICE_EN.fetchArticleString(articleName));
    }

    /**
     * Extracts plane-related information from an wikipedia article
     * @param article wikipedia article
     * @return plane information
     */
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
