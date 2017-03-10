package com.senacor.codecamp.reactive.concurrency;

public class Summary {

    public static void print(String planeNumber, int words, int rating, String numberBuild) {
        System.out.printf("Article on Plane %s has %s words, a rating of %s and %s planes have been build.\n", planeNumber, words, rating, numberBuild);
    }

    public static void printCounter(String planeNumber, String numberBuild) {
        System.out.printf("Article on Plane %s. %s planes have been build.\n", planeNumber, numberBuild);
    }

    public static void printCounter(String planeNumber, int numberBuild) {
        System.out.printf("Article on Plane %s. %s planes have been build.\n", planeNumber, numberBuild);
    }

    public static void printCounter(String[] planeInfo) {
        System.out.printf("Article on Plane %s. %s planes have been build.\n", planeInfo[0], planeInfo[1]);
    }
}
