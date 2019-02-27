package com.el.guavademo.functional;

import com.google.common.base.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Danfeng
 * @since 2019/2/27
 */
public class FunctionTest {

    @Test
    public void testFunc1() {
        Function<String, Integer> function = input -> {
            Preconditions.checkNotNull(input, "input can not to be null");
            return input.length();
        };

        function.apply("AAA");
    }

    @Test
    public void testFunc2() {
        String defaultStr = "K_T";
        Map<String, String> map = new HashMap<>();
        map.put("K1", "V1");
        map.put("K2", "V4");
        map.put("K3", "V5");
        Function<String, String> stringStringFunction = Functions.forMap(map, defaultStr);
        String k4 = stringStringFunction.apply("K4");
        System.out.println("k4===>" + k4);
    }

    @Test
    public void testFunc3() {
        Function<String, Long> compose = Functions.compose(input -> {
            Preconditions.checkNotNull(input, "input can not to be null");
            return (long) input;
        }, s -> s != null ? s.length() : 0);
        Long apply = compose.apply("ABC");
        assertThat(apply, equalTo(3L));
    }

    @Test
    public void testFunc4() throws InterruptedException {
        Supplier<String> memoize = Suppliers.memoize(() -> UUID.randomUUID().toString());
        AtomicReference<String> t1 = new AtomicReference<>("");
        AtomicReference<String> t2 = new AtomicReference<>("");
        System.out.println("====>");
        Thread thread1 = new Thread(() -> t1.getAndSet(memoize.get()));
        Thread thread2 = new Thread(() -> t2.getAndSet(memoize.get()));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("==t1==>"+t1);
        System.out.println("==t2==>"+t2);
        assertThat(t1.get(), equalTo(t2.get()));
    }
}
