package com.senacor.tecco.reactive.util;


import org.apache.commons.lang3.ClassUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public class DelayProxy extends DefaultProxyBehavior {

    public static <T> T newJdkProxy(Object obj, DelayFunction delayFunction) {
        Class<?> clazz = obj.getClass();
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
    protected Publisher<?> handlePublisherReturnType(Publisher<?> publisher, Method m, Object[] args) {
        return Mono.defer(() -> Mono.just(1).delayElementMillis(delayFunction.delay(m.getName())))
                .flatMap(next -> publisher);
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
