package com.senacor.tecco.reactive.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Andreas Keefer
 */
public class PingVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(PingVerticle.class);

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(500, msg -> {
            vertx.eventBus().send("ping", "ping", new DeliveryOptions().
                    setSendTimeout(500), reply -> {
                if (reply.succeeded()) {
                    log.info("Received respone: " + reply.result().body());
                } else {
                    log.info("Received no respone");
                }
            });
            try {
                // Simulate a delay
                Thread.sleep(450L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        vertx.setPeriodic(2000, msg -> {
            vertx.eventBus().publish("pingToLog", "ping");
        });
    }
}
