package com.itxiop.tech.supplier.sandbox.application.service;

import org.springframework.stereotype.Component;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides per-DUNS locking using a striped lock strategy.
 * A fixed pool of locks is maintained (no unbounded growth),
 * and each DUNS is mapped to one via consistent hashing.
 * The stripe count is chosen to be a power of two for efficient masking.
 */
@Component
public class DunsLockManager {

    private static final int STRIPE_COUNT = 1024;
    private static final int STRIPE_MASK = STRIPE_COUNT - 1;

    private final ReentrantLock[] stripes;

    public DunsLockManager() {
        stripes = new ReentrantLock[STRIPE_COUNT];
        for (int i = 0; i < STRIPE_COUNT; i++) {
            stripes[i] = new ReentrantLock();
        }
    }

    public ReentrantLock getLock(int duns) {
        return stripes[Math.abs(duns) & STRIPE_MASK];
    }
}
