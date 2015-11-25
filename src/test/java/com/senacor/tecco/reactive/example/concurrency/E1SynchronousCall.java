package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import static com.senacor.tecco.reactive.ReactiveUtil.print;
import org.junit.Test;

/**
 * Retrieves and combines plane information with synchronous method calls
 *
 * @author Dr. Michael Menzel, Sencaor Technologies AG
 */
public class E1SynchronousCall {

    @Test
    public void thatPlaneInfosAreCombinedSynchronous() throws Exception {

        String article777 = fetchArticle("Boeing 777");
        String numberBuilt777 = parseNumberBuilt(article777);

        String article747 = fetchArticle("Boeing 747");
        String numberBuilt747 = parseNumberBuilt(article747);

        String planesBuilt = "747: " + numberBuilt747 + " 777: " + numberBuilt777;

        print(planesBuilt);
    }


    String fetchArticle(String articleName) {
        return WikiService.WIKI_SERVICE_EN.fetchArticleString(articleName);
    }

    String parseNumberBuilt(String article){
        return WikiService.WIKI_SERVICE_EN.findValue(article, "number built");
    }


}
