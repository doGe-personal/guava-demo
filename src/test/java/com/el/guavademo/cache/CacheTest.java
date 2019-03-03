package com.el.guavademo.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Danfeng
 * @since 2019/2/27
 */
public class CacheTest {
    private Map<String, String> data = new HashMap<>();

    @Before
    public void beforeTest() {
        data.put("K1", "V1");
        data.put("K2", "k2");
    }

    @Test
    public void testCache1() {
        CacheLoader<String, String> cacheLoader = CacheLoader.from((i) -> StringUtils.isEmpty(i) ? "" : data.get(i));
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().build(cacheLoader);
        String k1 = loadingCache.getUnchecked("K1");
        assertThat("V1", equalTo(k1));
    }
    @Test
    public void testCachePreLoad() throws InterruptedException {
        CacheLoader<String, String> cacheLoader = CacheLoader.from(s -> s != null ? s.toLowerCase() : null);
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().build(cacheLoader);
        loadingCache.putAll(data);
        assertThat(loadingCache.size(), equalTo(2L));
    }


    @Test
    public void testCacheRefresh() throws InterruptedException {
        CacheLoader<String, Long> cacheLoader = CacheLoader.from((i) -> System.currentTimeMillis());
        LoadingCache<String, Long> loadingCache = CacheBuilder.newBuilder().refreshAfterWrite(2, TimeUnit.SECONDS).build(cacheLoader);
        Long r1 = loadingCache.getUnchecked("K1");
        Thread.sleep(3000);
        Long r2 = loadingCache.getUnchecked("K1");
        assertThat(r1.equals(r2), equalTo(true));
    }

    public static void main(String[] args) {
//        int t = 1000 ^ 1;
//        System.out.println(t);

        Integer a = 1;
        Integer b = 1;
        a=b=null;
        System.out.println(a+" "+b);

        while (true) {
            System.out.println("AAAAA");
        }
    }

}
