package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class WikiloaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiloaderApplication.class, args);
    }

    @Bean
    public WikiService wikiService() {
        return WikiService.create();
    }

    @Bean
    public CountService countService() {
        return CountService.create(DelayFunction.withNoDelay());
    }

    @Bean
    public RatingService ratingService() {
        return RatingService.create(DelayFunction.withNoDelay());
    }

    @Bean
    public Map<String, Article> fetchArticleCache() {
        return Collections.synchronizedMap(new LRUMap<>(20));
    }
}
