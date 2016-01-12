package com.senacor.tecco.reactive.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.buffer.impl.BufferFactoryImpl;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Andreas Keefer
 */
public class CodecVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(CodecVerticle.class);

    @Override
    public void start() throws Exception {
        vertx.eventBus().registerDefaultCodec(Person.class, new PersonMessageCodec());

        vertx.setPeriodic(1000, msg -> {
            vertx.eventBus().publish("codec-verticle", new Person("Hnas", 44));
        });
        vertx.eventBus().consumer("codec-verticle", msg -> {
            log.info("received Message on 'codec-verticle': " + msg.body());
        });
    }

    public static final class Person implements Serializable {
        private final String name;
        private final int age;

        public Person() {
            this(null, -1);
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", name)
                    .append("age", age)
                    .toString();
        }
    }

    public static final class PersonMessageCodec implements MessageCodec<Person, Person> {

        @Override
        public void encodeToWire(Buffer buffer, Person o) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                byte[] bytes = mapper.writeValueAsBytes(o);
                log.info("encodeToWire:" + mapper.writeValueAsString(o));
                buffer.appendInt(bytes.length);
                buffer.appendBytes(bytes);
            } catch (JsonProcessingException e) {
                log.error("Error encoding JSON", e);
                throw new IllegalStateException("Error encoding JSON", e);
            }
        }

        @Override
        public Person decodeFromWire(int pos, Buffer buffer) {
            int length = buffer.getInt(pos);
            byte[] bytes = buffer.getBytes(pos + 4, pos + 4 + length);
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(bytes, Person.class);
            } catch (IOException e) {
                log.error("Error decoding JSON", e);
                throw new IllegalStateException("Error decoding JSON", e);
            }
        }

        @Override
        public Person transform(Person o) {
            Buffer buffer = new BufferFactoryImpl().buffer();
            encodeToWire(buffer, o);
            Person decodeFromWire = decodeFromWire(0, buffer);
            return decodeFromWire;
            // Person is immutable, so just return the Person
            // return o;
        }

        @Override
        public String name() {
            return "Person";
        }

        @Override
        public byte systemCodecID() {
            return -1;
        }
    }
}
