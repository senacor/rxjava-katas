package com.senacor.tecco.codecamp.reactive;

import com.senacor.tecco.reactive.services.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author Andreas Keefer
 */
@RestController
public class WikiController {

    private final WikiService wikiService;

    @Autowired
    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @GetMapping("/article/{name}")
    public Flux<String> fetchArticle(@PathVariable String name) {
        return wikiService.fetchArticleFlux(name);
    }
}
