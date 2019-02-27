package com.el.guavademo.collections;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

/**
 * @author Danfeng
 * @since 2019/2/27
 */
public class CollectionTest {


    @Test
    public void testMultiset() {
        Multiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("d");
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("b");
        multiset.add("b");
        multiset.add("b");
        System.out.println("Occurrence of 'b' : "+multiset.count("b"));
        multiset.remove("b",2);
        System.out.println("Occurence of 'b' : "+multiset.count("b"));
    }
}
