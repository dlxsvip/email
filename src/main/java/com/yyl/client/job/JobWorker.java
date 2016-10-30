package com.yyl.client.job;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yl on 2016/10/9.
 */
public class JobWorker {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            20,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(256),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void addJob(Runnable job) {
        executor.execute(job);
    }
}
