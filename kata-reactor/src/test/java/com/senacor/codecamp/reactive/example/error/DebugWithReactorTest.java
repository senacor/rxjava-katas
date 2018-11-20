package com.senacor.codecamp.reactive.example.error;

import com.senacor.codecamp.reactive.util.WaitMonitor;
import org.junit.Before;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.joining;

/**
 * An example which shows the difficulties of debugging reactive streams.
 * Created by Daniel Heinrich on 01/03/2017.
 */
public class DebugWithReactorTest {
    private Random random = new Random();
    private String[] names = {"Daniel", "Andreas", "Michael", "Andri"};

    /**
     * A stream of the current system time in milliseconds
     *
     * @return a stream which produces every second a new value
     */
    private Flux<Long> getTime() {
//        return Flux.interval(Duration.of(1, SECONDS)).map(i -> System.currentTimeMillis());
        //we simulate some values to speed up the test
        return Flux.range(1, 6000).map(i -> i * 1000L);
    }

    /**
     * A stream which fires an event every whole minute. The events are the current system time in milliseconds.
     */
    private Flux<Long> everyMinute() {
        return getTime().filter(t -> MILLISECONDS.toSeconds(t) % 60 == 0);
    }

    private Flux<Integer> queryProductValues(String teamName, int factor, int max) {
        int maxHalf = max / 2;
        return Flux.range(random.nextInt(maxHalf), random.nextInt(maxHalf) + maxHalf)
                .map(i -> i / factor);
    }

    private Flux<String> queryTeam(String teamName) {
        return Flux.range(0, random.nextInt(10)).map(i -> names[random.nextInt(names.length)]);
    }

    private Mono<String> queryTeamReport(String teamName) {
        return queryTeam(teamName).collectList()
                .flatMap(team -> queryProductValues(teamName, team.size(), 100)
                        .reduce((a, b) -> a + b)
                        .map(sum -> "The team members:\n" +
                                team.stream().collect(joining(", ")) +
                                "\nproduced per member:\n" + sum + '\n')
                ).single();
    }

    private Mono<String> querySalesReport() {
        return queryProductValues("*", 1, 8).map(i -> "product with value: " + i)
                .reduce((a, b) -> a + '\n' + b)
                .map(r -> "Produced:\n" + r + '\n');
    }

    private Flux<String> reportsStream() {
        Flux<String> salesReport = everyMinute().flatMap(t -> querySalesReport());
        Flux<String> teamReport = everyMinute().flatMap(t -> queryTeamReport("LAB"));

        return Flux.merge(salesReport, teamReport);
    }

    private WaitMonitor monitor;

    @Before
    public void setup() {
        monitor = new WaitMonitor();
    }

    private void printAndComplete(Throwable t) {
        t.printStackTrace();
        monitor.complete();
    }


    /**
     * All the following executions of the reportsStreams will fail with an error. The tests will show
     * differences in the usefulness of the produced stack traces.
     * <p>
     * The following execution will run on a scheduler. The stacktrace will therefor only contain the
     * line where the error was thrown.
     */
    @Test
    public void onlyErrorPositionIsShown() {

        Disposable subscription = reportsStream()
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println, this::printAndComplete, monitor::complete);

        monitor.waitFor(2, TimeUnit.SECONDS);
        subscription.dispose();
    }

    /**
     * The produced stacktrace will contain the line where the error was thrown and where the subscription happened,
     * because the cold observable was executed on the main application thread.
     */
    @Test
    public void errorAndSubscribePositionIsShown() {
        WaitMonitor monitor = new WaitMonitor();

        Disposable subscription = reportsStream()
                .subscribe(System.out::println, this::printAndComplete, monitor::complete);

        monitor.waitFor(2, TimeUnit.SECONDS);
        subscription.dispose();
    }

    /**
     * The produced stack trace looks similar to the one from @{onlyErrorPositionIsShown}.
     * There are two things which are added.
     * <ul>
     * <li>
     * The normal stack trace shows below the line where the error happened
     * another will the position of the last operator used.
     * </li>
     * <li>
     * The debug hook will record all used operations and will print a operation stacktrace.
     * </li>
     * </ul>
     */
    @Test
    public void positionOfEveryOperatorIsShown() {
        WaitMonitor monitor = new WaitMonitor();

        Hooks.onOperatorDebug();

        Disposable subscription = reportsStream()
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println, this::printAndComplete, monitor::complete);

        monitor.waitFor(2, TimeUnit.SECONDS);
        subscription.dispose();
    }


}
