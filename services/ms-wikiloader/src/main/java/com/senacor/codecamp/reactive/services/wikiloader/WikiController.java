package com.senacor.codecamp.reactive.services.wikiloader;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.davidmoten.guavamini.Lists;
import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Andreas Keefer
 */
@RestController
@RequestMapping("/article")
public class WikiController {

    private final ArticleService articleService;

    @Autowired
    public WikiController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * This endpoint fetches an wikipedia article by name as media wiki text.
     *
     * @param name article name
     * @return article with media wiki as content
     */
    @GetMapping("/{name}")
    public Article fetchArticle(@PathVariable final String name) {
        // DUMMY
        return Article.newBuilder().withName(name).build();
    }

    /**
     * This endpoint streams an event for each article name, which is fetched by {@link #fetchArticle}.
     * This is a HOT Source (infinite stream) and acts as a Publisher in a publish/subscribe scenario.
     */
    @CrossOrigin
    @GetMapping("/readevents")
    @JsonView(Article.NameOnly.class)
    public List<Article> getReadStream() {
        // DUMMY
        return Lists.newArrayList(Article.newBuilder().withName("42").build());
    }
}
