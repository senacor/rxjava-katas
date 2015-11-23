package com.senacor.tecco.reactive.example.concurrency;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.senacor.tecco.reactive.ReactiveUtil.print;

/**
 * @author Dr. Michael Menzel
 */
public class E2Callback {

    // Funktion zur Fehlerbehandlung
    Consumer<Exception> exceptionConsumer = (e)->{e.printStackTrace();};

    @Test
    public void thatPlaneInfosAreCombineWithCallback() throws Exception {
        LinkedBlockingQueue<String> planesBuilt = new LinkedBlockingQueue<>();

        fetchArticle("Boeing 777", (article777) -> {

            fetchArticle("Boeing 747", (article747) -> {
                String numberBuilt777 = parseNumberBuilt (article777);
                String numberBuilt747 = parseNumberBuilt (article747);

                try {
                    planesBuilt.put("747: " + numberBuilt747 + " 777: " + numberBuilt777);
                    //TODO: javaslang
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, exceptionConsumer);
        }, exceptionConsumer);

        print(planesBuilt.poll(5, TimeUnit.SECONDS));
    }

    void fetchArticle(String articleName, Consumer<String> articleConsumer, Consumer<Exception> exceptionConsumer) {
        WikiService.WIKI_SERVICE_EN.fetchArticleCallback(articleName, articleConsumer, exceptionConsumer);
    }

    String parseNumberBuilt(String article){
        return WikiService.WIKI_SERVICE_EN.findValue(article, "number built");
    }

}
