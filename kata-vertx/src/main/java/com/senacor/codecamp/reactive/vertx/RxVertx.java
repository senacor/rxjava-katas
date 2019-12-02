package com.senacor.codecamp.reactive.vertx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.vertx.core.Vertx;
import io.vertx.core.streams.Pump;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.reactivestreams.ReactiveWriteStream;

/**
 * Created by dheinrich on 27/02/2017.
 * TODO (ak) umstellen auf native unterstuetzung von RxJava2 in Vert.x ab voraussichtl. 3.4.0
 */
public class RxVertx {

    /**
     * creates a observable of some address
     *
     * @param vertx
     * @param address the address which should be consumed
     * @return
     */
    public static Flowable<String> fromVertex(Vertx vertx, String address) {
        ReactiveWriteStream<String> writeStream = ReactiveWriteStream.writeStream(vertx);
        ReadStream<String> fetchArticle = vertx.eventBus().<String>consumer(address).bodyStream();
        Pump.pump(fetchArticle, writeStream).start();
        return Flowable.fromPublisher(writeStream);
    }

    /**
     * Sends a message on the Vertx eventbus and provieds a Obersvable of the reply.
     *
     * @param vertx
     * @param address the address to send the message to
     * @param msg     the message to send
     * @param <E>     the expected type of the reply
     * @return
     */
    public static <E> Observable<E> send(Vertx vertx, String address, Object msg) {
        PublishSubject<E> subject = PublishSubject.create();
        Observable<E> cachedReply = subject.cache();
        vertx.eventBus().<E>send(address, msg, event -> {
            if (event.succeeded()) {
                subject.onNext(event.result().body());
            } else {
                subject.onError(event.cause());
            }
            subject.onComplete();
        });
        return cachedReply;
    }
}
