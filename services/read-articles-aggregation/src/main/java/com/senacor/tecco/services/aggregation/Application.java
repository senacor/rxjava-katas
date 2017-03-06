package com.senacor.tecco.services.aggregation;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.DelayFunction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
