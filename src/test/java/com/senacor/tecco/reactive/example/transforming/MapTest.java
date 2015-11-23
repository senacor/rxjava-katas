package com.senacor.tecco.reactive.example.transforming;

import com.senacor.tecco.reactive.ReactiveUtil;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class MapTest {

    @Test
    public void testMain() throws Exception {
        create().map(vorname -> "vorname=" + vorname)
                .subscribe(ReactiveUtil::print);
    }

    public static Observable<String> create() {
        return Observable.just("Hans", "Bert", "Eugen", "Micky");
    }

}