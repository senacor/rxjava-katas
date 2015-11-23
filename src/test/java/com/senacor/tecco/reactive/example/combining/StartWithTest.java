package com.senacor.tecco.reactive.example.combining;

import com.senacor.tecco.reactive.ReactiveUtil;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class StartWithTest {

    @Test
    public void testStartWith() throws Exception {
        create().flatMap(vorname -> Observable.just(";").startWith(Observable.just(vorname)))
                .subscribe(ReactiveUtil::print);
    }

    public static Observable<String> create() {
        return Observable.just("Hans", "Bert", "Eugen", "Micky");
    }

}