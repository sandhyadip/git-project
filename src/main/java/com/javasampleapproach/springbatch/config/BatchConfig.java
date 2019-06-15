package com.javasampleapproach.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.javasampleapproach.springbatch.tasklet.NotificationPartitioner;
import com.javasampleapproach.springbatch.tasklet.EventServiceInvocationTaskletStep;
import com.javasampleapproach.springbatch.tasklet.EventStageCaptureTaskletStep;

@Configuration
public class BatchConfig {

	@Autowired
	private EventStageCaptureTaskletStep eventCaptureTaskletStep;

	@Autowired
	private EventServiceInvocationTaskletStep eventServiceInvocationTaskletStep;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;


	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(step1()).next(partitionStep())
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet(eventCaptureTaskletStep)
				.build();
	}

	@Bean
	public Step partitionStep() {
		return stepBuilderFactory.get("partitionStep")
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
	public Step slaveStep()  {
		return stepBuilderFactory.get("slaveStep")
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