package com.senacor.codecamp.reactive.services.integration;

/**
 * @author Andreas Keefer
 */
public class ArticleNotFoundException extends IllegalArgumentException {

    public ArticleNotFoundException() {
    }

    public ArticleNotFoundException(String s) {
        super(s);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotFoundException(Throwable cause) {
        super(cause);
    }
}
