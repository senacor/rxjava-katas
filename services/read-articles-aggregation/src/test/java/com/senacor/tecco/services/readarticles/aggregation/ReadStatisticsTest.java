package com.senacor.tecco.services.readarticles.aggregation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Daniel Heinrich on 07/03/2017.
 */
public class ReadStatisticsTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldBeSerializable() throws JsonProcessingException {
        ReadStatistics statistics = new ReadStatistics("hi", 3, 200);
        String json = objectMapper.writeValueAsString(statistics);
        assertNotNull(json);
    }
}