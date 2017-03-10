package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Daniel Heinrich
 * @since 08/03/2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ReadEventTest {

    @Mock
    private ArticleService service;
    private WikiController wikiController;

    @Before
    public void setup() {
        // setup mocks ...
    }

    @Test
    public void shouldEmitEventsOnRead() throws InterruptedException {

    }

    // ...
}
