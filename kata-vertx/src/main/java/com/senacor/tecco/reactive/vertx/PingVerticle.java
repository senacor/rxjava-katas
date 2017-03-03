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
    public static final String PING_ADRESS = "ping";
    public static final String PING_TO_LOG_ADRESS = "pingToLog";

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(500, msg -> {
            vertx.eventBus().send(PING_ADRESS, "ping", new DeliveryOptions().
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
            vertx.eventBus().publish(PING_TO_LOG_ADRESS, "ping");
        });
    }
}
