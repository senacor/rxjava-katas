/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.senacor.tecco.codecamp.reactive.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Subscribe to an observable or echoes uppercase content of text frames.
 *
 * @Autor Andri Bremm
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String,Supplier<Observable<?>>> observableMap = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

        if (frame instanceof TextWebSocketFrame) {

            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);

            // Subscribe to an observable
            if (observableMap.containsKey(request)) {
                subscribeChannelTo(ctx.channel(), observableMap.get(request).get());

            // ECHO: send the uppercase string back
            } else {
                String response = "ECHO: " + request;
                ctx.channel().writeAndFlush(new TextWebSocketFrame(response));
                logger.info("{} send {}", ctx.channel(), response);
            }
        } else {
            throw new UnsupportedOperationException("unsupported frame type: " + frame.getClass().getName());
        }
    }

    public ChannelHandler bind(String name, Supplier<Observable<?>> createObservable) {
        this.observableMap.put("#subscribe#" + name, createObservable);
        return this;
    }

    private <T> void subscribeChannelTo(Channel channel, Observable<T> observable) {
        observable
                .map(objectMapper::writeValueAsString)
                .subscribe(new Observer<String>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        logger.info("connected");
                        this.disposable = disposable;
                    }

                    @Override
                    public void onNext(String message) {
                        if (channel.isOpen()) {
                            channel.writeAndFlush(new TextWebSocketFrame(message));
                            logger.info("{} send {}", channel, message);
                        } else if (!disposable.isDisposed()) {
                            disposable.dispose();
                            logger.info("dispose");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String errorMessage = "#error#" + e.getMessage();
                        channel.writeAndFlush(new TextWebSocketFrame(errorMessage));
                        logger.info("{} send {}", channel, errorMessage);
                        e.printStackTrace();
                        channel.close();
                    }

                    @Override
                    public void onComplete() {
                        channel.close();
                    }
                });
    }
}
