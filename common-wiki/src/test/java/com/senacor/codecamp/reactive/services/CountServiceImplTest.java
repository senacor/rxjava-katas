package com.senacor.codecamp.reactive.services;

import com.senacor.codecamp.reactive.services.integration.MediaWikiTextParser;
import com.senacor.codecamp.reactive.util.FlakinessFunction;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;

import java.io.UncheckedIOException;

/**
 * @author Andreas Keefer
 */
public class CountServiceImplTest {

    private final ParsedPage parsedPage = new MediaWikiTextParser().parse(
            "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                    "== Religion ==\n" +
                    "\n" +
                    "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                    "== Weblinks ==\n" +
                    "\n" +
                    "{{commonscat|42}}");

    @Test(expected = UncheckedIOException.class)
    public void countWordsDefault() throws Exception {
        CountService countService = CountService.create(FlakinessFunction.alwaysFail());
        countService.countWords(parsedPage);
    }

}