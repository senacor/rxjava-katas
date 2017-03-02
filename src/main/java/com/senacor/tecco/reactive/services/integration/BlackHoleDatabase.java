package com.senacor.tecco.reactive.services.integration;

/**
 * @author Andreas Keefer
 */
public interface BlackHoleDatabase {
    /**
     * @param object article
     * @return runtime
     */
    long save(Object object);

    /**
     * @param objects article
     * @return runtime
     */
    long save(Iterable<Object> objects);
}
