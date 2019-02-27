package com.el.guavademo.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.condition.Join;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * @author Danfeng
 * @since 2019/2/26
 */
public class JoinerTest {

    private final List<String> stringList = Arrays.asList("Java", "Python", "Go", "PHP");
    private final List<String> stringListHasNull = Arrays.asList("Java", "Python", "Go", null);

    @Test
    public void testJoinOnJoin() {
        // 基于StringBuilder做遍历拼接，适用于数据量大的情况
        String result = Joiner.on("#").join(stringList);
        assertThat(result, equalTo("Java#Python#Go#PHP"));
        // JAVA8实现
        String useJava8 = stringList.stream().collect(Collectors.joining("#"));
        String useJava = String.join("#", stringList);

        assertThat(useJava8, equalTo("Java#Python#Go#PHP"));
        assertThat(useJava, equalTo("Java#Python#Go#PHP"));
    }

    @Test
    public void testJoinOnJoinWithNull() {
        String result = Joiner.on("#").skipNulls().join(stringListHasNull);
        assertThat(result, equalTo("Java#Python#Go"));
        // JAVA8实现
        String useJava8 = stringListHasNull.stream().filter(Objects::nonNull).collect(Collectors.joining("#"));
        assertThat(useJava8, equalTo("Java#Python#Go"));
        String def = Joiner.on("#").useForNull("DEF").join(stringListHasNull);
        assertThat(def, equalTo("Java#Python#Go#DEF"));
        // JAVA8实现
        String useJava = stringListHasNull.stream().map(item -> (Objects.isNull(item) || item.isEmpty()) ? "DEF" : item).collect(Collectors.joining("#"));
        assertThat(useJava, equalTo("Java#Python#Go#DEF"));
    }

    @Test
    public void testJoinAppend() {
        final StringBuilder stringBuilder = new StringBuilder();
        StringBuilder resultBuilder = Joiner.on("#").useForNull("DEF").appendTo(stringBuilder, stringListHasNull);
        assertThat(resultBuilder, sameInstance(stringBuilder));
        assertThat(resultBuilder.toString(), equalTo("Java#Python#Go#DEF"));
        assertThat(stringBuilder.toString(), equalTo("Java#Python#Go#DEF"));
    }

    private final Map<String, String> stringMap = ImmutableMap.of("K1", "V1", "K2", "V2");

    @Test
    public void testJoinWithMap() {
        assertThat(Joiner.on('#').withKeyValueSeparator("=").join(stringMap), equalTo("K1=V1#K2=V2"));
    }

}
