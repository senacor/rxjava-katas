package com.senacor.tecco.reactive.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public abstract class DefaultProxyBehavior implements InvocationHandler {

    private static String HASHCODE = "hashCode";
    private static String EQUALS = "equals";
    private static String TO_STRING = "toString";
    public static final List<String> EXCLUDES = Arrays.asList(HASHCODE, EQUALS, TO_STRING);

    protected DefaultProxyBehavior(Object target) {
        this.target = target;
    }

    private final Object target;

    public final Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (EXCLUDES.contains(m.getName())) {
            return m.invoke(getTarget(), args);
        }
        return invokeNotDelegated(proxy, m, args);
    }

    protected abstract Object invokeNotDelegated(Object obj, Method m, Object[] args) throws Throwable;

    protected Object getTarget() {
        return target;
    }
}