package com.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
public class IDGeneratorTest {

    // 清理环境
    @SuppressWarnings("unchecked")
	@org.junit.jupiter.api.BeforeEach
    public void setUp() {
        // 由于idStorage是私有静态变量，我们通过反射清空它
        try {
            java.lang.reflect.Field field = IDGenerator.class.getDeclaredField("idStorage");
            field.setAccessible(true);
            ((Set<String>) field.get(null)).clear();

            // 重置计数器
            java.lang.reflect.Field counterField = IDGenerator.class.getDeclaredField("counter");
            counterField.setAccessible(true);
            AtomicInteger counter= (AtomicInteger)counterField.get(null);
            counter.set(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerateUniqueID() throws IDGenerator.DuplicateIDException {
        String id1 = IDGenerator.generateID(300);
        String id2 = IDGenerator.generateID(300);
        assertNotEquals(id1, id2, "IDs should be unique within the same minute");
    }

    @Test
    public void testGenerateMaxIDPerMinute() {
        assertThrows(IDGenerator.DuplicateIDException.class, () -> {
            // Attempt to generate more IDs than allowed per minute
            for (int i = 0; i <= IDGenerator.MAX_ID_PER_MINUTE; i++) {
                IDGenerator.generateID(300);
            }
        }, "Should throw exception if more than 100 IDs are generated in a minute");
    }

    @Test
    public void testIDStoragePersistence() throws IDGenerator.DuplicateIDException {
        String id1 = IDGenerator.generateID(300);
        assertTrue(IDGenerator.idStorage.contains(id1), "Generated ID should be stored");
    }

    @Test
    public void testDuplicateIDException() throws InterruptedException {
        assertThrows(IDGenerator.DuplicateIDException.class, () -> {
           // String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            for (int i = 0; i < IDGenerator.MAX_ID_PER_MINUTE; i++) {
                IDGenerator.generateID(300);
            }
            // Simulate waiting for the same minute to generate duplicate
            Thread.sleep(100);
            IDGenerator.generateID(300);
        });
    }
}