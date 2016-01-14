package com.senacor.tecco.reactive.services;

import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import org.junit.Test;

public class MediaWikiTextParserTest {

    private final MediaWikiTextParser mediaWikiTextParser = new MediaWikiTextParser();

    @Test
    public void demonstrateUsage() {
        ParsedPage parsedPage = mediaWikiTextParser.parse(
                "{{Dieser Artikel|behandelt das Jahr 42, weitere Bedeutungen finden sich unter [[42 (Begriffsklärung)]].}}\n" +
                        "== Religion ==\n" +
                        "\n" +
                        "* Der [[Evangelist (Neues Testament)|Evangelist]] [[Markus (Evangelist)|Markus]] gründet laut kirchlicher Tradition den [[Patriarch von Alexandrien|Bischofssitz]] in [[Alexandria]]. \n" +
                        "== Weblinks ==\n" +
                        "\n" +
                        "{{commonscat|42}}");
        for (Link link : parsedPage.getLinks()) {
            System.out.println("link=" + link.getTarget());
        }
        for (Section section : parsedPage.getSections()) {
            System.out.println("Section: " + section.getTitle());
            for (Link link : section.getLinks(Link.type.INTERNAL)) {
                System.out.println(" sectionLink=" + link.getTarget());
            }
        }
    }

}