package com.senacor.codecamp.reactive.example.creating;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.reactivex.Observable;
import org.junit.Test;

/**
 * @author Andreas Keefer
 * @version 2.0
 */
public class ThrowTest {
    @Test
    public void testThrow() throws Exception {
        Observable.<String>error(new IllegalStateException("not yet implemented"))
                .subscribe(next -> ReactiveUtil.print("next: %s", next),
                        Throwable::printStackTrace,
                        () -> ReactiveUtil.print("complete!"));
    }
}