package com.senacor.codecamp.reactive.services.integration;

import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Andreas Keefer
 */
public class RatingBackendImpl implements RatingBackend {

    @Override
    public int rate(final ParsedPage parsedPage) {
        if (null == parsedPage) {
            throw new IllegalStateException("parsedPage must not be null");
        }
        int articleSize = parsedPage.getText().length();
        int linksCount = parsedPage.getLinks().size();

        if (0 == linksCount) {
            // 0 sterne
            return 0;
        }

        final BigDecimal percent = determinePercent(articleSize, linksCount);

        final int rating = determineRating(percent);

//        print("rate: articleSize=%s linksCount=%s percent=%s",
//                articleSize, linksCount, percent);
        return rating;
    }

    private BigDecimal determinePercent(int articleSize, int linksCount) {
        return BigDecimal.valueOf(linksCount)
                .divide(BigDecimal.valueOf(articleSize), 3, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private int determineRating(BigDecimal percent) {
        if (percent.compareTo(BigDecimal.valueOf(0.8)) > 0) {
            // 5 sterne
            return 5;
        } else if (percent.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            // 4 sterne
            return 4;
        } else if (percent.compareTo(BigDecimal.valueOf(0.3)) > 0) {
            // 3 sterne
            return 3;
        } else if (percent.compareTo(BigDecimal.valueOf(0.1)) > 0) {
            // 2 sterne
            return 2;
        } else {
            // 1 stern
            return 1;
        }
    }
}
