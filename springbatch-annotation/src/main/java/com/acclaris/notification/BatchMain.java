package com.acclaris.notification;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class BatchMain {
	
	
	public static void main(String[] args) {
		
		AbstractApplicationContext  context = new AnnotationConfigApplicationContext(BatchConfig.class);
	    
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
	    Job job = (Job) context.getBean("notificationJob");

	    try {
	        JobExecution execution = jobLauncher.run(job, new JobParameters());
	        System.out.println("Job Exit Status : "+ execution.getStatus());

	    } catch (JobExecutionException e) {
	        System.out.println("Job ExamResult failed");
	        e.printStackTrace();
	    }
		
	}
	
	


}