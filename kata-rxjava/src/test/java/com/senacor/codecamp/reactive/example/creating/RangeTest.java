package com.senacor.codecamp.reactive.example.creating;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class RangeTest {
    @Test
    public void testRange() throws Exception {
        Observable.range(4, 6)
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));
    }
}
