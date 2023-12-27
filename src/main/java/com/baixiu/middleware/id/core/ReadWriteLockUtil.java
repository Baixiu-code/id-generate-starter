package com.baixiu.middleware.id.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * @author baixiu
 * @date 2023年12月27日
 */
@Slf4j
@Component
public class ReadWriteLockUtil {

    private static final int DOUG_LEA_BLACK_MAGIC_OPERAND_1 = 20;
    private static final int DOUG_LEA_BLACK_MAGIC_OPERAND_2 = 12;
    private static final int DOUG_LEA_BLACK_MAGIC_OPERAND_3 = 7;
    private static final int DOUG_LEA_BLACK_MAGIC_OPERAND_4 = 4;

    /**
     * 序列Id读写锁数量
     */
    public static final int SEQ_ID_READ_WRITE_LOCK_NUM = 2048;

    public static final ReadWriteLock[] RW_LOCKS = new ReentrantReadWriteLock[SEQ_ID_READ_WRITE_LOCK_NUM];

    static {
        for (int i = 0; i < SEQ_ID_READ_WRITE_LOCK_NUM; i++) {
            RW_LOCKS[i] = new ReentrantReadWriteLock();
        }
        log.info("sequence id ReadWriteLock init completed......");
    }

    /**
     * 通过key获取锁的索引位置，相同的key对应相同的读写锁锁
     * @param key 需要锁的对象
     * @param numberOfLocks 锁的数量
     * @return 锁的索引位置
     */
    public static int selectLock(final Object key, int numberOfLocks) {
        int number = numberOfLocks & (numberOfLocks - 1);
        if (number != 0) {
            throw new RuntimeException ("Lock number must be a power of two: " + numberOfLocks);
        }
        if (key == null) {
            return 0;
        } else {
            return hash(key) & (numberOfLocks - 1);
        }

    }

    /**
     * 获取非空对象的hash值
     * @param object 需要计算hash值的对象
     * @return hash值
     */
    public static int hash(Object object) {
        int h = object.hashCode();
        h ^= (h >>> DOUG_LEA_BLACK_MAGIC_OPERAND_1) ^ (h >>> DOUG_LEA_BLACK_MAGIC_OPERAND_2);
        return h ^ (h >>> DOUG_LEA_BLACK_MAGIC_OPERAND_3) ^ (h >>> DOUG_LEA_BLACK_MAGIC_OPERAND_4);
    }
}
