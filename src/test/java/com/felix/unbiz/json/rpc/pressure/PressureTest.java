package com.felix.unbiz.json.rpc.pressure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.felix.unbiz.json.rpc.JettyServer;
import com.felix.unbiz.json.rpc.service.BookService;

/**
 * ClassName: PressureTest <br>
 * Function: PressureTest
 *
 * @author wangxujin
 */
public class PressureTest {

    private static final Random RANDOM = new Random();

    private JettyServer server;

    private BookService bookServiceProxy;

    private ExecutorService pool;

    private int invokeNum = 10000;

    public PressureTest() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        bookServiceProxy = (BookService) applicationContext.getBean("bookServiceProxy");
        server = new JettyServer();
        server.start(8088);
        pool = Executors.newFixedThreadPool(20);
    }

    public void test() {
        List<Callable<List<String>>> callableList = new ArrayList<Callable<List<String>>>(invokeNum);
        Callable<List<String>> c = new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return bookServiceProxy.getBooksById(Arrays.asList(RANDOM.nextInt(4)));
            }
        };
        for (int i = 0; i < invokeNum; i++) {
            callableList.add(c);
        }
        long start = System.currentTimeMillis();
        try {
            List<Future<List<String>>> futures = pool.invokeAll(callableList);
            for (Future<List<String>> f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        long timeTake = System.currentTimeMillis() - start;
        System.out.println("=========================== call time: " + timeTake + "ms");
        System.out.println("=========================== QPS:" + 1000f / ((timeTake) / (1.0f * invokeNum)));
    }

    public void close() {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        PressureTest pressure = new PressureTest();
        pressure.test();
        pressure.close();
    }
}
