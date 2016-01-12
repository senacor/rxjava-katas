package com.senacor.tecco.reactive.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Andreas Keefer
 */
public class PongVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(PongVerticle.class);

    private long counter = 0;

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("ping").handler(msg -> {
            counter++;
            msg.reply(msg.body() + " pong");
        });

        vertx.eventBus().consumer("pingToLog").handler(msg -> {
            log.info("Received a pingToLog. The counter value is " + counter);
        });

    }
}
