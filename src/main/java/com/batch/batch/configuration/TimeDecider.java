package com.batch.batch.configuration;

import java.time.LocalTime;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class TimeDecider implements JobExecutionDecider{

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		int hour = LocalTime.now().getHour();
        System.out.println("Current time: " + hour);
        if (hour >= 6 && hour < 18) {
            return new FlowExecutionStatus("MORNING");
        } else {
            return new FlowExecutionStatus("NIGHT");
        }
	}

}
