package com.el.guavademo.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Danfeng
 * @since 2019/2/26
 */
public class SplitterTest {

    @Test
    public void testSplitterOnSplit() {
        List<String> result = Splitter.on("|").splitToList("D|F");
        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("D"));
        assertThat(result.get(1),equalTo("F"));
        // 使用Java
        List<String> useJava = Arrays.asList(Objects.requireNonNull(StringUtils.split("D|F", "|")));
        assertThat(useJava.get(0),equalTo(result.get(0)));
    }

    @Test
    public void testSplitterOnSplitEmpty() {
        List<String> result = Splitter.on("|").omitEmptyStrings().splitToList("D|F||");
        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(2));
        assertThat(result.get(0),equalTo("D"));
        assertThat(result.get(1),equalTo("F"));
        String[] split = StringUtils.split("D|F||", "|");
        if((split != null ? split.length : 0) >0) {
            List<String> strings = Arrays.stream(split).filter(Objects::nonNull).collect(Collectors.toList());
            assertThat(result.size(),equalTo(strings.size()));
        }

    }
    @Test
    public void testSplitterOnFixedLength() {

        List<String> result = Splitter.fixedLength(4).splitToList("AAAABBBBCCCCDDDD");
        assertThat(result,notNullValue());
        assertThat(result.size(),equalTo(4));
        assertThat(result.get(0),equalTo("AAAA"));
        assertThat(result.get(3),equalTo("DDDD"));
        List<String> stringList = Splitter.on("#").limit(3).splitToList("AAAA#BBBB#CCCC#DDDD");
    }

}
