package com.senacor.tecco.reactive.util;

import com.google.common.base.Stopwatch;
import com.senacor.tecco.reactive.ReactiveUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Michael Omann
 * @author Andreas Keefer
 */
public class StopWatchProxy extends DefaultProxyBehavior {

    public static <T> T newJdkProxy(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(clazz);
        Object res = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                new StopWatchProxy(obj));
        return (T) res;
    }

    private StopWatchProxy(Object obj) {
        super(obj);
    }

    @Override
    protected Object invokeNotDelegated(Object proxy, Method m, Object[] args) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String methodName = printableMethod(m, args);
        Object result;
        try {
            result = m.invoke(this.getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception", e);
        } finally {
            ReactiveUtil.print("Invocation of method '%s' took %s", methodName, stopwatch.stop());
        }
        return result;
    }

    private static String printableMethod(Method m, Object[] args) {
        List<String> methodArgs = args == null ? Collections.emptyList() : Arrays.stream(args)
                .map(arg -> StringUtils.abbreviate(arg.toString(), 50).replaceAll("\\r\\n|\\r|\\n", " "))
                .collect(Collectors.toList());
        return m.getName() + (methodArgs.isEmpty() ? "" : (" with args " + methodArgs));
    }
}
