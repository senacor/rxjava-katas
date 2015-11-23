package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.Arrays;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class E5Streams {

    @Test
    public void thatPlaneInfosAreCombinedWithStreams() throws Exception {

        String[] planeTypes = {"Boeing 777", "Boeing 747"};

        Arrays.stream(planeTypes)
                .map(this::fetchArticle)
                .map(this::parseNumberBuilt)
                .forEach((numberBuilt)-> {
                    print(numberBuilt);
                });

    }


    String fetchArticle(String articleName) {
        return WikiService.WIKI_SERVICE_EN.fetchArticleString(articleName);
    }

    String parseNumberBuilt(String article){
        return WikiService.WIKI_SERVICE_EN.findValue(article, "number built");
    }


}
