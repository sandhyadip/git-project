package com.acclaris.notification;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.acclaris.notification.tasks.EventServiceInvocationTaskletStep;
import com.acclaris.notification.tasks.EventStageCaptureTaskletStep;
import com.acclaris.notification.tasks.NotificationPartitioner;
 
@Configuration
@EnableBatchProcessing
@ComponentScan({"com.acclaris.notification","com.acclaris.notification.tasks"})
public class BatchConfig {
	
	@Autowired
	private EventStageCaptureTaskletStep eventCaptureTaskletStep;

	@Autowired
	private EventServiceInvocationTaskletStep eventServiceInvocationTaskletStep;


    @Bean
    public JobRepository jobRepository() throws Exception{
        return new MapJobRepositoryFactoryBean().getObject();
    }
    
    @Bean(name="jobLauncher")
    public SimpleJobLauncher jobLauncher() throws Exception {
    	SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    	jobLauncher.setJobRepository(jobRepository());
    	return jobLauncher;
    }
    
    @Bean
    public JobBuilderFactory jobBuilderFactory() throws Exception {
    	return new JobBuilderFactory(jobRepository());
    }
    
    @Bean
    public ResourcelessTransactionManager transactionManager() {
    	return new ResourcelessTransactionManager();
    }
    @Bean
    public StepBuilderFactory stepBuilderFactory() throws Exception {
    	return new StepBuilderFactory(jobRepository(), transactionManager());
    }
    
	@Bean(name="notificationJob")
	public Job job()  throws Exception {
		return jobBuilderFactory().get("job")
				.incrementer(new RunIdIncrementer())
				.start(step1()).next(partitionStep())
				.build();
	}

	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory().get("step1")
				.tasklet(eventCaptureTaskletStep)
				.build();
	}

	@Bean
	public Step partitionStep() throws Exception {
		return stepBuilderFactory().get("partitionStep")
				.partitioner("slaveStep",partitioner())
					.step(slaveStep())
						.taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Partitioner partitioner() {
		Partitioner partitioner = new NotificationPartitioner();
		return partitioner;
	}

	@Bean
	public Step slaveStep() throws Exception {
		return stepBuilderFactory().get("slaveStep")
				.tasklet(eventServiceInvocationTaskletStep)
				.build();
	}
	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setCorePoolSize(5);
		taskExecutor.setQueueCapacity(5);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}

 
}