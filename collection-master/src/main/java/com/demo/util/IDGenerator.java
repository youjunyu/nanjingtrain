package com.demo.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IDGenerator {

    // 一个AtomicInteger用于控制每分钟内的ID计数
    private static final AtomicInteger counter = new AtomicInteger(0);
    // 每分钟最多生成100个ID
     static  int MAX_ID_PER_MINUTE = 100;
    // 格式化当前时间的格式化器
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    // 模拟数据库，用于存储生成的ID
   static final Set<String> idStorage = new HashSet<>();

    public static synchronized String generateID(int idcounter) throws DuplicateIDException {
        // 获取当前的分钟时间戳
        String timestamp = LocalDateTime.now().format(dateFormatter);
        
        // 获取当前计数并递增1
        int currentCount = counter.getAndIncrement();
        if(idcounter>MAX_ID_PER_MINUTE)
        {
        	  MAX_ID_PER_MINUTE = idcounter;
        }
      
        // 如果计数达到最大值，重置计数器
        if (currentCount >= MAX_ID_PER_MINUTE) {
            counter.set(0);
            currentCount = counter.getAndIncrement();
        }

        // 生成ID
        String id = timestamp + String.format("%02d", currentCount);

        // 检查ID是否已存在于存储中
        if (idStorage.contains(id)) {
            throw new DuplicateIDException("Duplicate ID: " + id);
        }

        // 存储ID
        idStorage.add(id);
        return id;
    }

    // 自定义异常类
    public static class DuplicateIDException extends Exception {
        public DuplicateIDException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        // 测试生成ID
        try {
            for (int i = 0; i < 800; i++) {
                String id = generateID(300);
                //System.out.println("Generated ID: " + id);
                try {
                    // 简单的模拟100ms间隔
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (DuplicateIDException e) {
            e.printStackTrace();
        }
    }
}
