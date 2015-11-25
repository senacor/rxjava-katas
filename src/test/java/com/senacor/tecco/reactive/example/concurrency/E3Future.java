package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.Future;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * Retrieves and combines plane information  with futures
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E3Future {

    @Test
    public void thatPlaneInfosAreCombinedWithFuture() throws Exception {

        Future<String> article777Future = fetchArticle("Boeing 777");
        Future<String> article747Future = fetchArticle("Boeing 747");

        String article747 = article747Future.get();
        String article777 = article777Future.get();

        String numberBuilt777 = parseNumberBuilt(article747);
        String numberBuilt747 = parseNumberBuilt(article777);

        String planesBuilt = "747: " + numberBuilt747 + " 777: " + numberBuilt777;

        print(planesBuilt);
    }

    Future<String> fetchArticle(String articleName){
        return WikiService.WIKI_SERVICE_EN.fetchArticleFuture(articleName);
    }

    String parseNumberBuilt(String article){
        return WikiService.WIKI_SERVICE_EN.findValue(article, "number built");
    }


}
