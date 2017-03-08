package com.senacor.tecco.reactive.util;


import org.apache.commons.lang3.ClassUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public class DelayProxy extends DefaultProxyBehavior {

    public static <T> T newJdkProxy(T obj, DelayFunction delayFunction) {
        Class<T> clazz = (Class<T>) obj.getClass();
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                clazz.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                new DelayProxy(obj, delayFunction));
    }

    private final DelayFunction delayFunction;

    private DelayProxy(Object obj, DelayFunction delayFunction) {
        super(obj);
        this.delayFunction = delayFunction;
    }

    @Override
    protected Object handlePublisherReturnType(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            Publisher<?> publisher = (Publisher) m.invoke(this.getTarget(), args);
            Flux<?> res = Mono.defer(() -> Mono.just(1).delayElementMillis(delayFunction.delay(m.getName())))
                    .flatMap(next -> publisher);

            if (publisher instanceof Mono) {
                return res.single();
            } else if (publisher instanceof Flux) {
                return res;
            }
            throw new IllegalArgumentException("Publisher not supported: " + publisher.getClass().getName());
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
    }

    @Override
    protected Object invokeNotDelegated(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            long delay = delayFunction.delay(m.getName());
            if (0 < delay) {
                Thread.sleep(delay);
            }
            return m.invoke(this.getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
    }
}
