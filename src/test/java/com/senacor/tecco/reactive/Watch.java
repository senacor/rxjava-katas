package com.senacor.tecco.reactive;


import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

public class Watch extends Stopwatch {
    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }


    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }


    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }


    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        System.out.println(String.format("Test [%s] %s, spent %d ms",
                testName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
    }

}