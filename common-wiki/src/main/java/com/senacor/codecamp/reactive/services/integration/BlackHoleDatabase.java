package com.senacor.codecamp.reactive.services.integration;

/**
 * @author Andreas Keefer
 */
public interface BlackHoleDatabase {
    /**
     * @param object article
     * @return runtime
     */
    long saveOne(Object object);

    /**
     * @param objects article
     * @return runtime
     */
    long saveBatch(Iterable<?> objects);
}
