package com.senacor.tecco.services.metrics;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
@RestController
@RequestMapping("/metrics")
public class MetricsEndpoint {

    private final CountService countService;
    private final RatingService ratingService;
    private final WikiService wikiService;

    @Autowired
    public MetricsEndpoint(CountService countService, RatingService ratingService, WikiService wikiService) {
        this.countService = countService;
        this.ratingService = ratingService;
        this.wikiService = wikiService;
    }

    @PostMapping("/wordcount")
    public int getWordCount(@RequestBody String article) {
        ParsedPage parsedPage = wikiService.parseMediaWikiText(article);
        return countService.countWords(parsedPage);
    }

    @PostMapping("/rating")
    public int getRating(@RequestBody String article) {
        ParsedPage parsedPage = wikiService.parseMediaWikiText(article);
        return ratingService.rate(parsedPage);
    }
}
