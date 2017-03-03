package com.senacor.tecco.reactive.example.transforming;

import com.senacor.tecco.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class MapTest {

    @Test
    public void testMap() throws Exception {
        create().map(vorname -> "vorname=" + vorname)
                .subscribe(ReactiveUtil::print);
    }

    public static Observable<String> create() {
        return Observable.just("Hans", "Bert", "Eugen", "Micky");
    }

}