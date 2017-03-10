package com.senacor.codecamp.reactive.katas.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author Andreas Keefer
 */
public class ParsedPageCodec implements MessageCodec<ParsedPage, ParsedPage> {

    private static final Logger log = LoggerFactory.getLogger(ParsedPageCodec.class);
    private ObjectMapper mapper;

    public ParsedPageCodec() {
        this.mapper = new ObjectMapper();
        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(ANY)
                .withGetterVisibility(NONE)
                .withSetterVisibility(NONE)
                .withCreatorVisibility(NONE)
                .withIsGetterVisibility(NONE));
    }

    @Override
    public void encodeToWire(Buffer buffer, ParsedPage o) {
        try {

            byte[] bytes = mapper.writeValueAsBytes(o);
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } catch (JsonProcessingException e) {
            log.error("Error encoding JSON", e);
            throw new IllegalStateException("Error encoding JSON", e);
        }
    }

    @Override
    public ParsedPage decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        byte[] bytes = buffer.getBytes(pos + 4, pos + 4 + length);
        try {
            return mapper.readValue(bytes, ParsedPage.class);
        } catch (IOException e) {
            log.error("Error decoding JSON", e);
            throw new IllegalStateException("Error decoding JSON", e);
        }
    }

    @Override
    public ParsedPage transform(ParsedPage o) {
        return o;
    }

    @Override
    public String name() {
        return "ParsedPageJacksonMessageCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
