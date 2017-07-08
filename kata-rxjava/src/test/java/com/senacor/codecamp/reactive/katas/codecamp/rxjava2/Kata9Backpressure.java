package com.senacor.codecamp.reactive.katas.codecamp.rxjava2;

import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.advanced;
import static com.senacor.codecamp.reactive.katas.KataClassification.Classification.mandatory;
import static com.senacor.codecamp.reactive.util.ReactiveUtil.print;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.senacor.codecamp.reactive.katas.KataClassification;
import com.senacor.codecamp.reactive.services.PersistService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.util.DelayFunction;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import com.senacor.codecamp.reactive.util.WaitMonitor;

import io.reactivex.Flowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DefaultSubscriber;


/**
 * @author Andreas Keefer
 */
public class Kata9Backpressure {

    private final WikiService wikiService = WikiService.create(DelayFunction.withNoDelay(),
            FlakinessFunction.noFlakiness(), true, "de");
    private final PersistService persistService = PersistService.create();

    /**
     * run this with -Xmx64m
     */
    public static void main(String[] args) throws Exception {
        RxJavaPlugins.setErrorHandler(error -> {
            error.printStackTrace();
            System.exit(1);
        });
        new Kata9Backpressure().backpressure();
    }

    @KataClassification({mandatory, advanced})
    private void backpressure() throws Exception {
        // 1. run the main method with -Xmx64m and recognize the OutOfMemoryError.
        // 2. change the readWikiArticlesFromFile to a producer which handles backpressure.
        // 3. change the consumer to signal the producer his needs/capacity.

        WaitMonitor monitor = new WaitMonitor();
        //String fileName = "articles.100.txt";
        //String fileName = "articles.1000.txt";
        String fileName = "articles.10000.txt";
        //String fileName = "articles.100000.txt";
        //String fileName = "articles.1000000.txt";

//        Disposable subscriber = readWikiArticlesFromFile(fileName)
//        		.flatMap(article -> )
        
        readWikiArticlesFromFile(fileName)
        	.doOnNext(next -> print("reading article: %s", next))
        	.flatMap(article -> wikiService.fetchArticleFlowable(article)
        			.subscribeOn(Schedulers.io()))
        	.subscribeOn(Schedulers.io())
        	.subscribe(new DefaultSubscriber<String>() {

                @Override
                protected void onStart() {
                    request(10);
                }

				@Override
				public void onComplete() {
					// TODO Auto-generated method stub
					print("complete"); 
					monitor.complete();
					
				}

				@Override
				public void onError(Throwable t) {
					// TODO Auto-generated method stub
					t.printStackTrace();
					
				}

				@Override
				public void onNext(String article) {
					// TODO Auto-generated method stub
					persistService.save(article); 
					request(1);
					
				}
			}); 
        		
        		
        		
//                .doOnNext(next -> print("reading article: %s", next))
//                .flatMap(article -> wikiService.fetchArticleObservable(article)
//                        .subscribeOn(Schedulers.io()))
//                .subscribeOn(Schedulers.io())
//                .subscribe(persistService::save,
//                        Throwable::printStackTrace,
//                        () -> {
//                            print("complete!");
//                            monitor.complete();
//                        });

        monitor.waitFor(120, SECONDS);
//        subscriber.dispose();
    }

    /**
     * read Articles from file
     *
     * @param fileName File name (e.g. "articles.100.txt")
     * @return stream of Article names from File (line-by-line)
     */
    private Flowable<String> readWikiArticlesFromFile(String fileName) {
        return Flowable.generate(() -> 
        	Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI())), 
        	(bufferedReader, emitter) -> {
        		String line = "";
				try {
					line = bufferedReader.readLine();
				} catch (IOException e) {
					emitter.onError(e);
				} 
        		if (line != null) {
        			emitter.onNext(line);
        		} else {
        			emitter.onComplete();
        		}
	        }, bufferedReader -> {
	        	try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }); 
        		
        		
        		
//        		.create(emitter -> {
//            BufferedReader bufferedReader;
//            try {
//                bufferedReader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
//            } catch (IOException | URISyntaxException e) {
//                emitter.onError(e);
//                return;
//            }
//            try {
//                String line = bufferedReader.readLine();
//                while (line != null && !emitter.isCancelled()) {
//                    emitter.onNext(line);
//                    line = bufferedReader.readLine();
//                }
//                emitter.onComplete();
//            } catch (IOException ex) {
//                emitter.onError(ex);
//            } finally {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, BackpressureStrategy.LATEST);
    }
}