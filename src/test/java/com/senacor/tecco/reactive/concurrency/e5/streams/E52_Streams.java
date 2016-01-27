package com.senacor.tecco.reactive.concurrency.e5.streams;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Retrieves and combines plane information with streams
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E52_Streams {
    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneBuildCountIsFetchedWithStreams() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747"};

        Arrays.stream(planeTypes)
                //.parallel()
                .map(planeType -> fetchArticle(planeType))
                .map(article -> parsePlaneInfo(article))
                .forEach((planeInfo)-> {
                    Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
                });
    }


    /**
     * fetches an article from the wikipedia
     * @param articleName name of the wikipedia article
     * @return an article
     */
    Article fetchArticle(String articleName) {
        return new Article(articleName, wikiService.fetchArticle(articleName));
    }

    /**
     * Extracts plane-related information from an wikipedia article
     * @param article wikipedia article
     * @return plane information
     */
    PlaneInfo parsePlaneInfo(Article article){
        return new PlaneInfo(article.name, ReactiveUtil.findValue(article.content, "number built"));
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
