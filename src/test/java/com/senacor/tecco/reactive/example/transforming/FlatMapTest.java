package com.senacor.tecco.reactive.example.transforming;

import com.senacor.tecco.reactive.ReactiveUtil;
import io.reactivex.Observable;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class FlatMapTest {

    @Test
    public void testFlatMap() throws Exception {
        create().flatMap(name -> Observable.fromArray(StringUtils.split(name, ", ")))
                .subscribe(ReactiveUtil::print);
    }

    public static Observable<String> create() {
        return Observable.just("Wurst, Hans", "Bummler, Bert", "Eber, Eugen", "Maus, Micky");
    }

}