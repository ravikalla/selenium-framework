package com.bdd.utilities;

import com.google.inject.AbstractModule;

import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.IExecutionContext;

public class ExecutionBinding extends AbstractModule{
	
	public ExecutionBinding() {
		
	}
	
	public void configure() {
		
		this.bind(IExecutionContext.class).to(ExecutionContext.class);
	}

}
