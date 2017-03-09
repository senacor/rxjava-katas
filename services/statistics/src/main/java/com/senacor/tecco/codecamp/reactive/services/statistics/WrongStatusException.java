package com.senacor.tecco.codecamp.reactive.services.statistics;

import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.function.Consumer;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Daniel Heinrich
 */
public class WrongStatusException extends RuntimeException {
    private final ClientResponse reponse;

    public WrongStatusException(ClientResponse reponse) {
        super(reponse.statusCode() + ": " + reponse.statusCode().getReasonPhrase());
        this.reponse = reponse;
    }

    public static Consumer<ClientResponse> okFilter() {
        return r -> {
            if(r.statusCode() != OK)
            {
                throw new WrongStatusException(r);
            }
        };
    }
}
