package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.WikiService;
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
    private WikiService service;

    @Before
    public void setup() {

    }

    @Test
    public void shouldEmitEventsOnRead() throws InterruptedException {

    }

}
