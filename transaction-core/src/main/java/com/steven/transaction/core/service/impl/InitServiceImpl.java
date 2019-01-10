package com.steven.transaction.core.service.impl;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * InitServiceImpl.
 *
 * @author steven
 */
public class InitServiceImpl {
    public static void main(String[] args) {
        new LinkedBlockingQueue<>(10);
    }
}
