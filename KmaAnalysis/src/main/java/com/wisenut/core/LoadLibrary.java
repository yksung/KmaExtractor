package com.wisenut.core;

import java.lang.reflect.Field;

public class LoadLibrary {
	
	static {
		try {
			setLibraryPath(System.getProperty("jni_path"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			System.loadLibrary("wkosl");
		}catch(UnsatisfiedLinkError e){
			System.err.println("The dynamic link library for Java could not be loaded."
					+ "\nConsider using \n java -Djava.library.path=\n"+e.getMessage());
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void setLibraryPath(String path) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		System.setProperty("java.library.path", path);
		final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
		sysPathsField.setAccessible(true);
		sysPathsField.set(null, null);
	}
}
