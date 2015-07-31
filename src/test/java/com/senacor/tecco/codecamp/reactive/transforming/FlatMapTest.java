package com.senacor.tecco.codecamp.reactive.transforming;

import com.senacor.tecco.codecamp.reactive.ReactiveUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import rx.Observable;

/**
 * @author Andreas Keefer
 */
public class FlatMapTest {

    @Test
    public void testFlatMap() throws Exception {
        create().flatMap(name -> Observable.from(StringUtils.split(name, ", ")))
                .subscribe(ReactiveUtil::print);
    }

    public static Observable<String> create() {
        return Observable.just("Wurst, Hans", "Bummler, Bert", "Eber, Eugen", "Maus, Micky");
    }

}