package com.demo.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author junze.yu
 * @version 1.0
 * 
 */
public class IDGeneratorNoStorage {
    
    // 一个AtomicInteger用于控制每分钟内的ID计数
    private static final AtomicInteger counter = new AtomicInteger(0);
    // 每分钟最多生成100个ID
    private static final int MAX_ID_PER_MINUTE = 100;
    // 格式化当前时间的格式化器
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    
    /**
     * ID生成算法类
     * generateID 
     * @return
     */
    public static synchronized String generateID() {
        // 获取当前的分钟时间戳
        String timestamp = LocalDateTime.now().format(dateFormatter);
        
        // 获取当前计数并递增1
        int currentCount = counter.getAndIncrement();
        
        // 如果计数达到最大值，重置计数器
        if (currentCount >= MAX_ID_PER_MINUTE) {
            counter.set(0);
            currentCount = counter.getAndIncrement();
        }
        
        // 生成ID
        return timestamp + String.format("%02d", currentCount);
    }
    
    public static void main(String[] args) {
       
    	// 测试生成ID
        for (int i = 0; i < 120; i++) 
        {
        	
            String id = generateID();
            System.out.println("Generated ID: " + id);
            
            try 
            {
            
                // 简单的模拟100ms间隔
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}