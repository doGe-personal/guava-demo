# Guava学习心得
		Guava是一种基于开源的Java库，从源码查看感受到其标准简洁以及对代码有高度的优化。避免了开发过程中充分造轮子。
	通过查看源码可以学习到“设计模式运用”,“工具类方法的编写规范”,“代码性能优化”,“最佳实践”等很多相关的知识点，是一套
	值得学习研究的源码库。不过具体是否应用依据项目而定，在Java8环境下，Java8同样提供一下工具类和函数式接口与guava有
	重叠的功能,类似Iava8有Collections,Arrays,Optional,StringUtils,Stream,Consumer等。所以在Java中没有的工具类
	封装时参考Guava的相关实现是一个不错的选择。通过这两天我对guava几个核心模块做了测试学习，心得如下:

## 1.字符串集合转换相关(Joiner,Splitter,CharMatcher,CaseFormat)
  - Joiner: 主要做集合转换成字符串，提供静态方法on(String separator) | on(char separator) 
		  设计为生成器模式[link](https://projectlombok.org/api/lombok/experimental/Builder.html),
		  字符串拼接使用StringBuilder(非线程安全)性能较好
		  但观察其工具方法多可用Java8 stream流式操作能达到相同的效果，比如：
	```text
        1) private final List<String> stringListHasNull = Arrays.asList("Java", "Python", "Go", null);
           String result = Joiner.on("#").skipNulls().join(stringListHasNull);
           // JAVA8实现
           String useJava8 = stringListHasNull.stream().filter(Objects::nonNull).collect(Collectors.joining("#")); 
        2) String def = Joiner.on("#").useForNull("DEF").join(stringListHasNull);
           // JAVA8实现
           String useJava = stringListHasNull.stream().map(item -> (Objects.isNull(item) || item.isEmpty()) ? "DEF" : item)
          .collect(Collectors.joining("#"));
    ```
  - Splitter: 主要做字符串转换成集合(支持正则)，并用final修饰符修饰(类不可更改和继承)，方法内实例化具体行为，属性定义为接口或
              抽象类(CharMatcher,Strategy)策略模式和迭代器模式的体现,使用抽象类CharMatcher,可自定义matches策略, 值得学习其设计模式
              及算法思想。
              不过Splitter大部分方法可实现并不复杂，例如：
    ```text
         1）List<String> result = Splitter.on("|").splitToList("D|F");
            List<String> useJava = Arrays.asList(Objects.requireNonNull(StringUtils.split("D|F", "|")));      // 使用Java
         2）List<String> result = Splitter.on("|").omitEmptyStrings().splitToList("D|F||");
            String[] split = StringUtils.split("D|F||", "|");
            if((split != null ? split.length : 0) >0) {
                List<String> strings = Arrays.stream(split).filter(Objects::nonNull).collect(Collectors.toList());
            }
    ```
##	2.函数式编程(函数式强调用函数的方式改变对象的状态)
	    Guava的函数式接口继承了Java8的相应功能的接口，做到兼容性。与Java8一致面向接口编程的方式。
	但Java8缺少对各个函数式的整合。
  - Function<F, T>: 定义参数类型(入参F，出参T),跟 Java8保持一致，用法也相同。
  - Functions: 相当于函数式接口的工厂,利用枚举单例模式并利用了懒加载的特性，例如ToStringFunction,IdentityFunction...
			其中几个较为实用的接口为: ForMapWithDefault (为Map.get()设置默认值),PredicateFunction(类似Java8的定义Predicate),
	```text		
        Functions.compose(Function<B, C> g, Function<A, ? extends B> f)
        eg: 
            Function<String, Long> compose = Functions.compose(input -> {
            Preconditions.checkNotNull(input, "input can not to be null");
                return (long) input;
            }, s -> s != null ? s.length() : 0);
            assertThat(compose.apply("ABC"),equalTo(3L));
	     		
	    等等，guava相当于对Java几种函数式做了整合。
	``` 
  -	Suppliers: 指定返回类型。实现了备忘录工具方法。比如MemoizingSupplier做了一层懒加载，之前入参之后，下次调用get()得到相同的值。
	    	volatile 修饰 变量initialized，保证多线程之间的可见性。并使用的单例模式的双重检查锁，保证了多线程运行时的数据一致性。
  ```text
        @Override
        public T get() {
          // A 2-field variant of Double Checked Locking.
          if (!initialized) { // 第一次判断
            synchronized (this) {  // 加锁
              if (!initialized) { // 第二次判断
                T t = delegate.get();
                value = t;
                initialized = true;
                return t;
              }
            }
          }
          return value;
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
  ```
       
       
##  3.Guava Cache
        Guava提供了一个基于Jvm的缓存工具库。实现了很多缓存优化的算法，比如LRU缓存机制(Java中LinkHashMap有类似实现)
        LocalCache继承Java并发包的java.util.concurrent.ConcurrentMap保证并发量(类似Java的ConcurrentHashMap)，
        CashLoader(抽象类,并定义from方法入参函数式接口作为数据来源),CacheBuilder(生成器模式，构建LoadingCache的实现类),
        LoadingCache(Cache的核心接口,定义子类接口的行为get(),refresh()等):三个核心类或接口，
       
   ```text
        数据预加载：
        测试如下:
            @Test
            public void testCachePreLoad() throws InterruptedException {
                CacheLoader<String, String> cacheLoader = CacheLoader.from(s -> s != null ? s.toLowerCase() : null);
                LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().build(cacheLoader);
                loadingCache.putAll(data);
                assertThat(loadingCache.size(), equalTo(2L));
            }
      
        缓存刷新：
            Guava cache可以设置超时时间，如果超时则重新从Provider里获取数据
            测试如下： 
            @Test
            public void testCacheRefresh() throws InterruptedException {
                CacheLoader<String, Long> cacheLoader = CacheLoader.from((i) -> System.currentTimeMillis());
                LoadingCache<String, Long> loadingCache = CacheBuilder.newBuilder().refreshAfterWrite(2, TimeUnit.SECONDS).build(cacheLoader);
                Long r1 = loadingCache.getUnchecked("K1");
                Thread.sleep(3000);
                Long r2 = loadingCache.getUnchecked("K1");
                assertThat(r1.equals(r2), equalTo(true));
            }
            类似redis缓存时的expire超时设置。
   ```
##  4.集合工具
        Guava提供了几个比较实用的集合工具(Multiset,Multimap,BiMap,Table)。
        Multiset: HashMultiset.create();方法构建，查看源码HashMultiset继承父类的标准构造方法
        AbstractMapBasedMultiset(Map<E, Count> backingMap);
        内部维护了一个Map，key为Multiset的元素，Count为计数器，实现了序列化，并重写了equals和hashCode方法，
        并且有final修饰符修饰，保证实例化之后不会被更改，每个key对应一个唯一的Count计数器，
        类似ThreadLocal.ThreadLocalMap 维护了一个线程内部变量。
##  5.其他
        Optional,Preconditions与Java8中的Optional,Predicate相关，Preconditions对常用的一些Predicate做了封装。


