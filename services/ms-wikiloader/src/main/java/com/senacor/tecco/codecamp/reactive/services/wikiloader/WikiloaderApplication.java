package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.reactive.services.WikiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WikiloaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiloaderApplication.class, args);
    }

    @Bean
    public WikiService wikiService() {
        return WikiService.create();
    }
}
