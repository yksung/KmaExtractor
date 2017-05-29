package com.wisenut.server;

import java.util.concurrent.ThreadFactory;

public class PoolThreadFactory implements ThreadFactory {

	private final String name;
 
	public PoolThreadFactory(String name) {
		this.name = name;
	}
 
	@Override
	public Thread newThread(final Runnable r) {
		final PoolAppThread poolAppThread = new PoolAppThread(r, name);
		PoolAppThread.setDebug(true);    
		return poolAppThread;
	}
}