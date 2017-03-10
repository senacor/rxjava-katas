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

    @GetMapping("/{name}")
    public Article fetchArticle(@PathVariable final String name) {
        // DUMMY
        return Article.newBuilder().withName(name).build();
    }

    @CrossOrigin
    @GetMapping("/readevents")
    @JsonView(Article.NameOnly.class)
    public List<Article> getReadStream() {
        // DUMMY
        return Lists.newArrayList(Article.newBuilder().withName("42").build());
    }
}
