package com.senacor.codecamp.reactive.util;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public abstract class DefaultProxyBehavior implements InvocationHandler {

    public static final List<String> EXCLUDE_METHOD_NAMES = Arrays.asList("hashCode", "equals", "toString");

    protected DefaultProxyBehavior(Object target) {
        this.target = target;
    }

    private final Object target;

    public final Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (EXCLUDE_METHOD_NAMES.contains(m.getName())) {
            return m.invoke(getTarget(), args);
        } else if (Publisher.class.isAssignableFrom(m.getReturnType())) {
            Publisher<?> publisher = (Publisher) m.invoke(this.getTarget(), args);

            Publisher<?> res = handlePublisherReturnType(publisher, m, args);
            if (publisher instanceof Mono) {
                return Mono.from(res);
            } else if (publisher instanceof Flux) {
                return Flux.from(res);
            }
            throw new IllegalArgumentException("Publisher not supported: " + publisher.getClass().getName());
        }
        return invokeNotDelegated(proxy, m, args);
    }

    protected abstract Publisher<?> handlePublisherReturnType(Publisher<?> publisher, Method m, Object[] args);

    protected abstract Object invokeNotDelegated(Object obj, Method m, Object[] args) throws Throwable;

    protected Object getTarget() {
        return target;
    }
}