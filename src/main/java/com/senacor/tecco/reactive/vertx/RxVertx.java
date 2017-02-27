package com.senacor.tecco.reactive.vertx;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.vertx.core.Vertx;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;

/**
 * Created by dheinrich on 27/02/2017.
 */
public class RxVertx {

    public static Flowable<String> fromVertex(Vertx vertx, String adress) {
        ReactiveWriteStream<String> writeStream = ReactiveWriteStream.writeStream(vertx);
        ReadStream<String> fetchArticle = vertx.eventBus().<String>consumer("fetchArticle").bodyStream();
        Pump.pump(fetchArticle, writeStream).start();
        return Flowable.fromPublisher(writeStream);
    }

    public static <E> Observable<E> send(Vertx vertx, String adress, Object msg) {
        PublishSubject<E> subject = PublishSubject.create();
        vertx.eventBus().<E>send(adress, msg, event -> {
            if (event.succeeded()) {
                subject.onNext(event.result().body());
            } else {
                subject.onError(event.cause());
            }
            subject.onComplete();
        });
        return subject;
    }
}
