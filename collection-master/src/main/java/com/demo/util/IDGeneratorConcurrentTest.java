package com.demo.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IDGeneratorConcurrentTest {

    @BeforeEach
    public void setUp() {
        // 清理ID存储和重置计数器
        try {
            java.lang.reflect.Field field = IDGenerator.class.getDeclaredField("idStorage");
            field.setAccessible(true);
            ((Set<String>) field.get(null)).clear();

            java.lang.reflect.Field counterField = IDGenerator.class.getDeclaredField("counter");
            counterField.setAccessible(true);
            ((AtomicInteger) counterField.get(null)).set(0);
            
            System.out.println("数量："+IDGenerator.idStorage.size());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentIDGeneration() throws InterruptedException {
        int numThreads = 3000; // 每分钟3000个用户
        
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        ConcurrentSkipListSet<String> generatedIDs = new ConcurrentSkipListSet<>();

        Callable<Void> idGenerationTask = () -> {
            try {
                String id = IDGenerator.generateID(numThreads);
                generatedIDs.add(id);
            } catch (IDGenerator.DuplicateIDException e) {
                // 异常处理，打印
            	//生成环境使用log4j打印 
            	
                e.printStackTrace();
            }
            return null;
        };

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(idGenerationTask);
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(1, TimeUnit.MINUTES); // 等待所有任务完成

        assertTrue(finished, "ExecutorService did not terminate in the expected time");
        assertEquals(numThreads, generatedIDs.size(), "Generated IDs should be unique");

        executorService.shutdownNow(); // 再次强制关闭，以防有未完成任务
    }
    
    @Test
    public void testConcurrentIDGenerationFor() throws InterruptedException {
        int numThreads = 300; // 每分钟300个用户
        int idCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        ConcurrentSkipListSet<String> generatedIDs = new ConcurrentSkipListSet<>();

        Callable<Void> idGenerationTask = () -> {
            try {
                String id = IDGenerator.generateID(idCount);
                generatedIDs.add(id);
            } catch (IDGenerator.DuplicateIDException e) {
                // 异常处理，打印
            	//生成环境使用log4j打印 
            	
                e.printStackTrace();
            }
            return null;
        };

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(idGenerationTask);
        }

        executorService.shutdown();
        boolean finished = executorService.awaitTermination(1, TimeUnit.MINUTES); // 等待所有任务完成

        assertTrue(finished, "ExecutorService did not terminate in the expected time");
        System.out.println("数量："+generatedIDs.size());
        assertEquals(numThreads, generatedIDs.size(), "Generated IDs should be unique");
      //  assertEquals(idCount, generatedIDs.size(), "ID数量相等");

        executorService.shutdownNow(); // 再次强制关闭，以防有未完成任务
    }
}
