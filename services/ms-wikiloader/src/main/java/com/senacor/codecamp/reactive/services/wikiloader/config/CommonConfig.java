package com.senacor.codecamp.reactive.services.wikiloader.config;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
