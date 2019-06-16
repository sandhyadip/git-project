package com.acclaris.notification.tasks;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EventServiceInvocationTaskletStep  implements Tasklet,StepExecutionListener {

	@Value("#{stepExecutionContext[record]}") 
	String record;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try{
			
    		System.out.println("****** Inside EventServiceInvocationTaskletStep ***** "+record);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		System.out.println("After EventServiceInvocationTaskletStep ");
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {
		System.out.println("Before EventServiceInvocationTaskletStep ");
		
	}
}
