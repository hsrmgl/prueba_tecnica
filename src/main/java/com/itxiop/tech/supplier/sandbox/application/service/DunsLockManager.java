package com.itxiop.tech.supplier.sandbox.application.service;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DunsLockManager {
    private final ConcurrentHashMap<Integer, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ReentrantLock getLock(int duns) {
        return locks.computeIfAbsent(duns, k -> new ReentrantLock());
    }
}
