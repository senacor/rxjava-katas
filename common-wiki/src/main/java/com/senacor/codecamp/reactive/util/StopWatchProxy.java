package com.senacor.codecamp.reactive.util;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.ClassUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

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

    public static <T> T newJdkProxy(Object obj) {
        Class<?> clazz = obj.getClass();
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
    protected Publisher<?> handlePublisherReturnType(Publisher<?> publisher, Method m, Object[] args) {
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        Flux<?> res = Flux.from(publisher)
                .doOnSubscribe(subscription -> stopwatch.start())
                .doOnTerminate(() -> finished(m, args, stopwatch));
//        Flux<?> res = Mono.defer(() -> Mono.just(1).doOnNext(integer -> stopwatch.start()))
//                .flatMap(next -> publisher)
//                .doOnTerminate(() -> finished(m, args, stopwatch));
        return res;
    }

    @Override
    protected Object invokeNotDelegated(Object proxy, Method m, Object[] args) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object result;
        try {
            result = m.invoke(this.getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception", e);
        } finally {
            finished(m, args, stopwatch);
        }
        return result;
    }

    private void finished(Method m, Object[] args, Stopwatch stopwatch) {
        ReactiveUtil.print("Invocation of method '%s' took %s", printableMethod(m, args), stopwatch.stop());
    }

    private static String printableMethod(Method m, Object[] args) {
        List<String> methodArgs = args == null ? Collections.emptyList() : Arrays.stream(args)
                .map(arg -> ReactiveUtil.abbreviateWithoutNewline(arg.toString(), 50))
                .collect(Collectors.toList());
        return m.getName() + (methodArgs.isEmpty() ? "" : (" with args " + methodArgs));
    }
}
