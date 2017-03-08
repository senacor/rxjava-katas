package com.senacor.tecco.reactive.util;

import org.reactivestreams.Publisher;

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
        } else if (Publisher.class.isAssignableFrom(m.getReturnType())){
            return handlePublisherReturnType(proxy, m, args);
        }
        return invokeNotDelegated(proxy, m, args);
    }

    protected abstract Object handlePublisherReturnType(Object proxy, Method m, Object[] args) throws Throwable;

    protected abstract Object invokeNotDelegated(Object obj, Method m, Object[] args) throws Throwable;

    protected Object getTarget() {
        return target;
    }
}