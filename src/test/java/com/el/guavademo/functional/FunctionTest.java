package com.el.guavademo.functional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.junit.Test;

import javax.annotation.Nullable;

/**
 * @author Danfeng
 * @since 2019/2/27
 */
public class FunctionTest {


    @Test
    public void testFunc1() {

        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                Preconditions.checkNotNull(input, "input can not to be null");
                return input.length();
            }
        };

        function.apply("AAA");
    }

    @Test
    public void testFunc2() {

        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                Preconditions.checkNotNull(input, "input can not to be null");
                return input.length();
            }
        };

        function.apply("AAA");
    }

}
