package com.senacor.tecco.reactive.util;

import org.apache.commons.lang3.ClassUtils;

import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public class FlakyProxy extends DefaultProxyBehavior {

    public static <T> T newJdkProxy(T obj, FlakinessFunction flakinessFunction) {
        Class<T> clazz = (Class<T>) obj.getClass();
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                clazz.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                new FlakyProxy(obj, flakinessFunction));
    }

    private final FlakinessFunction flakinessFunction;

    private FlakyProxy(Object obj, FlakinessFunction flakinessFunction) {
        super(obj);
        this.flakinessFunction = flakinessFunction;
    }

    @Override
    protected Object invokeNotDelegated(Object proxy, Method m, Object[] args) throws Throwable {
        try {
            flakinessFunction.failOrPass(m.getName());
            return m.invoke(this.getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (UncheckedIOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
    }
}
