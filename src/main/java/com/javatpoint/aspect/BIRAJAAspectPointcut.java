
package com.javatpoint.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.stereotype.Component;
@Component

@Aspect
public class BIRAJAAspectPointcut {

	//@Pointcut("execution(public String getUserName())")
	 @Pointcut("execution(* com.javatpoint.beans*.*())")
	public void getNamePointcut(){
		System.out.println("huare trigger");
		
	}
	@Before("getNamePointcut()")
	public void loggingAdvice(){
		System.out.println("Executing loggingAdvice on getName()");
	}
	
	@Before("getNamePointcut()")
	public void secondAdvice(){
		System.out.println("Executing secondAdvice on getName()");
	}
	
	
	/*
	@Before("allMethodsPointcut()")
	public void allServiceMethodsAdvice(){
		System.out.println("Before executing service method");
	}
	
	//Pointcut to execute on all the methods of classes in a package
	@Pointcut("within(com.journaldev.spring.service.*)")
    public void allMethodsPointcut(){}
    
    */
	
}
