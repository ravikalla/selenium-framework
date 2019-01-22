package com.bdd.utilities;

import java.util.ArrayList;
import java.util.Arrays;

import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;

public class RunCukes {

	public RunCukes() {
		
	}
	
	public void RunningOfCukes(String[] argv) throws Exception {
		
		run(argv, Thread.currentThread().getContextClassLoader());
		
	}

	public static byte run(String[] argv, ClassLoader ClassLoader) throws Exception {
		
		ResourceLoader resourceLoader = new MultiLoader(ClassLoader);
		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, ClassLoader);
		RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList(Arrays.asList(argv)));
		Runtime runTime = new Runtime(resourceLoader, classFinder, ClassLoader, runtimeOptions);
		runTime.run();
		return runTime.exitStatus();
	}
	
}
