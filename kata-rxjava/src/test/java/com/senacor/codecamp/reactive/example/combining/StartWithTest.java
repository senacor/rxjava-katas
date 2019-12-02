package com.senacor.codecamp.reactive.example.combining;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
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