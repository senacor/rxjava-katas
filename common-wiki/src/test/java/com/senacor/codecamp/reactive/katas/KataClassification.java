package com.senacor.codecamp.reactive.katas;

/**
 * @author Andreas Keefer
 */
public @interface KataClassification {

    Classification[] value();

    enum Classification {
        /**
         * everyone should complete this Kata
         */
        mandatory,

        /**
         * optional Kata: if you are looking for a challenge
         */
        advanced,

        /**
         * optional Kata: for the brave
         */
        hardcore,

        /**
         * optional Kata: mess with the best, die like the rest
         */
        nightmare
    }
}
