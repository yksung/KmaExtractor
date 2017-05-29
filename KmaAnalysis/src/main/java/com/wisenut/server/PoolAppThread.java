package com.wisenut.server;

import java.util.concurrent.atomic.AtomicInteger;

public class PoolAppThread extends Thread {

 public  static final String DEFAULT_POOL_NAME  = "PoolAppThread";
 private static volatile boolean debugLifecycle  = false;
 private static final AtomicInteger created   = new AtomicInteger();
 private static final AtomicInteger alive   = new AtomicInteger();
 
 public PoolAppThread(Runnable r) {
  this(r, DEFAULT_POOL_NAME);  
 }
 
 public PoolAppThread(Runnable runnable, String name) {
        super(runnable, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t,
                                          Throwable e) {
             
             System.out.println("UNCAUGHT in thread " + t.getName() + ":" + e);
            }
        });
    }
 
 public void run() {
        // Copy debug flag to ensure consistent value throughout.
        boolean debug = debugLifecycle;
        if (debug) {
         System.out.println("Created : " + getName());         
        }
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) {
             System.out.println("Exiting : " + getName());
            }
        }
    }
 
 public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }
}