package com.senacor.tecco.reactive.katas.introduction;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.katas.introduction.Kata2FetchArticleObservable.Article;
import com.senacor.tecco.reactive.services.WikiService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import rx.Observable;

/**
 * @author Dr. Michael Menzel
 */
public class Kata3TransformObservable {
	private final WikiService wikiService = new WikiService("en");

	@Test
	public void createAnObservable() throws Exception {
		String[] planeTypes = { "Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330", "Airbus A320 family" };

		// 1) create an observable that emits the plane type
		// 2) use the fetchArticleObservable method to transform the plane type
		// to an Article
		// 3) use the parsePlaneInfo method to transform the article to an
		// planeInfo object
		// 4) subscribe to the observable and print the plane information

		Observable.from(planeTypes).flatMap(plane -> fetchArticle(plane)).map(article -> parsePlaneInfo(article))
				.subscribe(ReactiveUtil::print);

	}

	/**
	 * fetches an article from the wikipedia
	 * 
	 * @param articleName
	 *            name of the wikipedia article
	 * @return an article
	 */
	Observable<Article> fetchArticle(String articleName) {
		return wikiService.fetchArticleObservable(articleName).map((article) -> new Article(articleName, article));
	}

	/**
	 * Extracts plane-related information from an wikipedia article
	 * 
	 * @param article
	 *            wikipedia article
	 * @return plane information
	 */
	PlaneInfo parsePlaneInfo(Article article) {
		return new PlaneInfo(article.name, ReactiveUtil.findValue(article.content, "number built"));
	}

	class Article {
		public String name;
		public String content;

		public Article(String name, String content) {
			this.name = name;
			this.content = content;
		}
	}

	class PlaneInfo {
		public String typeName;
		public String numberBuild;

		public PlaneInfo(String typeName, String numberBuild) {
			this.typeName = typeName;
			this.numberBuild = numberBuild;
		}

		@Override
		public String toString() {
			return "PlaneInfo{" + "typeName='" + typeName + '\'' + ", numberBuild='" + numberBuild + '\'' + '}';
		}
	}

}
