package com.senacor.codecamp.reactive.example.transforming;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
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