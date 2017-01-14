package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static rx.Observable.zip;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

	private WikiService wikiService = new WikiService();
	private CountService countService = new CountService();
	private RatingService ratingService = new RatingService();

	@Test
	public void combiningObservable() throws Exception {
		// 1. fetch and parse Wikiarticle
		// 2. use ratingService.rateObservable() and
		// countService.countWordsObervable(). Combine both information as JSON
		// and print the JSON to the console. Example {"articleName":
		// "Superman", "rating": 3, "wordCount": 452}

		// wikiService.fetchArticleObservable()
		String article = "Wikipedia";
		wikiService.fetchArticleObservable(article).flatMap(wikiService::parseMediaWikiTextObservable)
				.flatMap(parsedPage -> {
					Observable<Integer> rating = ratingService.rateObservable(parsedPage);
					Observable<Integer> counting = countService.countWordsObervable(parsedPage);

					return zip(rating, counting, (r, c) -> String
							.format("{\"articleName\": %s, \"rating\":%s, \"wordCount\":%s}", article, r, c));
				}).subscribe(ReactiveUtil::print, Throwable::printStackTrace, () -> print("complete!"));

	}

}
