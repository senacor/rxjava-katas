package com.senacor.codecamp.reactive.example.transforming;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
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