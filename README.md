# json-rpc [![Build Status](https://travis-ci.org/wangxujin/json-rpc.svg?branch=master)](https://travis-ci.org/wangxujin/json-rpc)  [![Coverage Status](https://coveralls.io/repos/github/wangxujin/json-rpc/badge.svg?branch=master)](https://coveralls.io/github/wangxujin/json-rpc?branch=master)

json-rpc是一种无状态的，轻量级的远程过程调用（RPC）协议，在GSON协议之上增加Spring容器集成、负载均衡、容错处理、注解服务发现等特性。

## 服务端开发
#### 工程创建

* json-rpc-unbiz使用的是基于长连接的http协议来进行RPC调用的，服务器端需要把自己的代码部署在jetty、tomcat中。
创建一个war包maven工程，配置WEB-INF/web.xml,加入json-rpc servlet配置，如下所示：
	
	```
	<servlet>
	    <servlet-name>json rpc servlet</servlet-name>
	    <servlet-class>com.felix.unbiz.json.rpc.server.JsonRpcServlet</servlet-class>
	    <load-on-startup>1</load-on-startup>
	</servlet>
	 
	<servlet-mapping>
	    <servlet-name>json rpc servlet</servlet-name>
	    <url-pattern>/api/*</url-pattern>
	</servlet-mapping>
	```
	
#### 暴露服务

* 服务接口暴露以“类”为级别，可以把实现某个接口的类中所有方法用json-rpc-unbiz暴露出去，注意每个接口的定义的方法名要求唯一，不允许重载。例如，提供了一个接口，通过书的编号ID获取书的名称的服务

	```
	public interface BookService {
	    String getBookById(int id);
	}
	```
	其实现类BookServiceImpl如下：
	
	```
	@JsonRpcService(serviceInterface = BookService.class)
	@Service
	public class BookServiceImpl implements BookService {
	     // 实现方法
	}
	```
	其中@Service为spring bean的注解暴露；注解@JsonRpcService表示暴露该bean为json-rpc-unbiz的服务，参数serviceInterface表示该bean实现的接口，客户端可以通过rpc调用方式访问通信。
	
* json-rpc-unbiz还提供了xml方式的暴露服务的方式，xml方式和注解方式，默认注解方式优先级高于xml。
	
	```
	<bean id="bookService" class="com.felix.beidou.server.test.jsonrpc.BookServiceImpl"></bean>
	 
	<bean class="com.felix.unbiz.json.rpc.server.JsonRpcExporter" scope="singleton">
	       <property name="serviceInterfaceName" value="com.felix.beidou.server.test.jsonrpc.BookService"></property>
	       <property name="serviceBean" ref="bookService"></property>
	</bean>
	```	
	
#### 服务启动
浏览器打开如下链接 [http://ip:port/api](http://ip:port/api)

## 客户端开发
#### 依赖服务端接口

* 推荐一般项目使用这种客户端调用方式，依赖服务端发布的SDK jar，可以做到只依赖其暴露的VO/BO/接口/异常信息等，减少和服务端的耦合。
* 如果服务端不是java语言，则没有SDK，需要在客户端定义服务端接口，可以如下定义。
  
	```
	public interface BookService {
	    String getBookById(int id);
	}
	```
	
#### 配置客户端代理

* 基于注解的方式配置服务

	Spring3.0以后，XML 配置方式不再是 Spring IoC 容器的唯一配置方式，如今使用注解，配置信息的载体由 XML 文件转移到了 Java 类中。通常将用于存放配置信息的类的类名以 “Config” 结尾，比如 AppDaoConfig.java、AppServiceConfig.java 等等。需要在用于指定配置信息的类上加上 @Configuration 注解，以明确指出该类是 Bean 配置的信息源。
		
		
	```
	@Configuration
	public class JsonRpcConfig {
	 
	    @Bean
	    public BookService bookService() throws Exception {
	        return (BookService) new ProxyFactoryBean(BookService.class, "127.0.0.1:8088", "/api/BookService",
	                getHeaderMap()).getObject();
	    }
	 
	    // 此处产生头部认证信息，如果没有可以为null或空
	    private Map<String, String> getHeaderMap() {
	        return ImmutableMap.of("syscode", "wwqe1233", "token", "21313092321");
	    }
	}
	```
	
* 基于XML方式配置服务
	配置需要加载到Spring容器中，需要在applicationContext.xml中加入如下配置
	
	```
	<bean id="bookService" class="com.felix.unbiz.json.rpc.client.ProxyFactoryBean">
	    <property name="serviceInterface">
	        <value>com.felix.beidou.test.jsonrpc.server.BookService</value>
	    </property>
	    <property name="serviceUrl" value="${BOOK_SERVICE_URL}"/>
	    <property name="servers" value="${BOOK_SERVER}"/>
	    <property name="connectionTimeout" value="5000"></property>
	    <property name="readTimeout" value="5000"></property>
	    <property name="headerMap">
	        <map>
	            <entry key="syscode" value="wwqe1233" />
	            <entry key="token" value="21313092321" />
	        </map>
	    </property>
	</bean>
	```
	
#### 调用服务
可以用Spring的原生方式注入到任何类中，例如通过@Resource或者@Autowire注解。

```
@Controller
public class BookController {
 
    private static final Random RANDOM = new Random();
 
    @Autowired
    private BookService bookService;
 
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public Performance test(@RequestParam(value = "thread", defaultValue = "20") int threadNum,
                            @RequestParam(value = "num", defaultValue = "10000") int invokeNum) {
 
        return ConcurrentPerformance.testConcurrentCall(invokeNum, threadNum, new ConcurrentPerformance.Work<String>() {
            @Override
            public String doWork() {
                return bookService.getBookById(RANDOM.nextInt(3));
            }
        });
    }
 
}
```
	

