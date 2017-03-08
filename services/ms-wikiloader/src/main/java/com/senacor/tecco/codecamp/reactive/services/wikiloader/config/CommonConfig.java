package com.senacor.tecco.codecamp.reactive.services.wikiloader.config;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.util.DelayFunction;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

/**
 * @author Andreas Keefer
 */
@Configuration
public class CommonConfig {

    @Bean
    public CountService countService() {
        return CountService.create(DelayFunction.withNoDelay());
    }

    @Bean
    public RatingService ratingService() {
        return RatingService.create(DelayFunction.withNoDelay());
    }

    @Bean
    public Map<String, Article> fetchArticleCache(@Value("${article.cache.size}") int cacheSize) {
        return Collections.synchronizedMap(new LRUMap<>(cacheSize));
    }
}
