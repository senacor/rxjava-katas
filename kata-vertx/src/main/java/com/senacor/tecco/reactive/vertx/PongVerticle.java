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
        vertx.eventBus().consumer(PingVerticle.PING_ADRESS).handler(msg -> {
            counter++;
            msg.reply(msg.body() + " pong");
        });

        vertx.eventBus().consumer(PingVerticle.PING_TO_LOG_ADRESS).handler(msg -> {
            log.info("Received a pingToLog. The counter value is " + counter);
        });

    }
}
