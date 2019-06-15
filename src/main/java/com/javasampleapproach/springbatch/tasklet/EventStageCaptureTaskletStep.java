package com.javasampleapproach.springbatch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class EventStageCaptureTaskletStep implements Tasklet,StepExecutionListener {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try{
    		System.out.println("Inside EventStageCaptureTaskletStep ");
    		List<Integer> recordList = new ArrayList<>();
			recordList.add(1);
			recordList.add(2);
			recordList.add(3);
			recordList.add(4);
			recordList.add(5);
			recordList.add(6);
			recordList.add(7);
			recordList.add(8);
			recordList.add(9);
			recordList.add(10);
    		NotificationPartitioner.setRecordList(recordList);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		System.out.println("After EventStageCaptureTaskletStep ");
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {
		System.out.println("Before EventStageCaptureTaskletStep ");
		
	}
}