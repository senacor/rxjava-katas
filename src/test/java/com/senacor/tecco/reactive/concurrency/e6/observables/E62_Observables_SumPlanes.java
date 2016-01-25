package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;

public class E62_Observables_SumPlanes {

    private final WikiService wikiService = new WikiService("en");

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneBuildSumIsCalculatedWithObservables() throws Exception {

        //String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};
        String[] planeTypes = {"Boeing 777", "Boeing 747"};

        Observable.from(planeTypes)
                .flatMap(this::fetchArticle)
                .map(this::parsePlaneInfo)
                .reduce(this::reducePlaneInfo)
                .subscribe((planeInfo) -> {
                    Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
                });
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

        /**
         * Extracts plane-related information from an wikipedia article
         * @param article wikipedia article
         * @return plane information
         */
        PlaneInfo parsePlaneInfo(Article article){
            String buildCount = ReactiveUtil.findValue(article.content, "number built");
            return new PlaneInfo(article.name, Integer.parseInt(buildCount.replaceAll(",","")));
        }

        /**
         * reduces plane information by summing up the build counter
         * @param planeInfoSum summed up plane information
         * @param planeInfo plane information
         * @return summed up plane information
         */
        PlaneInfo reducePlaneInfo(PlaneInfo planeInfoSum, PlaneInfo planeInfo){
            planeInfoSum.numberBuild += planeInfo.numberBuild;
            planeInfoSum.appendTypeName(" and " + planeInfo.typeName);
            return planeInfoSum;
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
            public int numberBuild;

            public PlaneInfo() {
                this.typeName = "";
                this.numberBuild = 0;
            }

            public PlaneInfo(String typeName, int numberBuild) {
                this.typeName = typeName;
                this.numberBuild = numberBuild;
            }

            public void appendTypeName(String name){
                this.typeName += name;
            }

        }
}
