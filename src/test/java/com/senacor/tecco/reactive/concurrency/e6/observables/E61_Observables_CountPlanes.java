package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.Watch;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import net.sourceforge.jwbf.core.contentRep.Article;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;

public class E61_Observables_CountPlanes {

    private final WikiService wikiService = new WikiService("en");
    private final CountService countService = new CountService();
    private final RatingService ratingService = new RatingService();

    @Rule
    public final Watch watch = new Watch();

    @Test
    public void thatPlaneInfoIsCombinedWithObservables_notPerfectYet() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family"};

        Observable.from(planeTypes)
                .flatMap(this::fetchArticle)
                .map(this::parsePlaneInfo)
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
        }
}
