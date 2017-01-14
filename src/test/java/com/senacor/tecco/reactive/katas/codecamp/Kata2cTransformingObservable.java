package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.WikiService;

import rx.Observable;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata2cTransformingObservable {

	private WikiService wikiService = new WikiService();
	private CountService countService = new CountService();

	@Test
	public void transformingObservable() throws Exception {
		// 1. Use the WikiService (fetchArticleObservable) and fetch an
		// arbitrary wikipedia article
		// 2. transform the result with the WikiService#parseMediaWikiText to an
		// object structure
		// 3. print the number of words that begin with character 'a' to the
		// console (ParsedPage.getText())

		// wikiService.fetchArticleObservable()

		Observable.just("Computer").flatMap(value -> wikiService.fetchArticleObservable(value))
				.flatMap(text -> Observable.from(wikiService.parseMediaWikiText(text).getText().split(" "))).filter(word -> word.startsWith("a")).count()
				.subscribe(ReactiveUtil::print, Throwable::printStackTrace, () -> print("complete!"));
	}

}
