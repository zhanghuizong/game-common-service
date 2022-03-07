package com.bitgame.game.framework.util;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncThreadUtil {
    protected static ThreadPoolExecutor threadPool = null;
    protected static int minPoolSize = 5;
    protected static int maxPoolSize = 50;
    protected static int blockPoolSize = 50;
    protected static int idlePoolSecond = 30;

    static {
        threadPool = new ThreadPoolExecutor(minPoolSize, maxPoolSize, idlePoolSecond, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(blockPoolSize), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
