package com.acclaris.notification.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class NotificationPartitioner implements Partitioner {
	
	private static List<Integer> recordList;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		System.out.println("gridSize is "+gridSize);
		Map<String, ExecutionContext> map = new HashMap<>(gridSize);
		Integer threadCount = 0;
		for (Integer record : recordList) {
			ExecutionContext context = new ExecutionContext();
            context.putString("record", String.valueOf(record));
            map.put("PARTITION_KEY" + ++threadCount, context);
		}
		return map;
	}
	
	public static void setRecordList(List<Integer> recordList) {
		NotificationPartitioner.recordList=recordList;
	}

}
