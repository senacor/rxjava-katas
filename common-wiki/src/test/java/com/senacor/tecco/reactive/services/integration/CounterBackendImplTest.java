package com.senacor.tecco.reactive.services.integration;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class CounterBackendImplTest {

    @Test
    public void countWords() throws Exception {
        assertEquals(0, countOf(""));
        assertEquals(0, countOf(" "));
        assertEquals(1, countOf("asd "));
        assertEquals(1, countOf(" asd"));
        assertEquals(1, countOf(" asd "));
        assertEquals(1, countOf("asd\tasd"));
        assertEquals(2, countOf("sdf asd"));
        assertEquals(2, countOf("sdf  asd"));
        assertEquals(2, countOf("sdf   asd"));
    }

    private int countOf(String text) {
        return new CounterBackendImpl().countWords(mock(text));
    }

    private ParsedPage mock(String text) {
        return new ParsedPage(){
            @Override
            public String getText() {
                return text;
            }
        };
    }
}