package com.senacor.tecco.codecamp.reactive.services.wikiloader.config;

import com.senacor.tecco.reactive.services.WikiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Andreas Keefer
 */
@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public WikiService wikiService() {
        return WikiService.create();
    }
}
