package com.senacor.tecco.codecamp.reactive.services.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * A listener for {@link ApplicationReadyEvent} to provide a hint to find the frontend.
 *
 * @author Andri Bremm
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsApplication.class);

    @Value("${server.port}")
    private String serverPort;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Find frontend at http://localhost:{}/index.html", serverPort);
    }
}
