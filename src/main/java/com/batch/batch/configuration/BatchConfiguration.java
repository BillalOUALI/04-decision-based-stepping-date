package com.batch.batch.configuration;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Configuration
public class BatchConfiguration {
	
	

	@Bean
	Step startStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("startStep", jobRepository).tasklet((contribution, chunkContext) -> {
			log.info("This is the start tasklet");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}

	@Bean
	Step morningStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("morningStep", jobRepository).tasklet((contribution, chunkContext) -> {
			log.info("This is the morning tasklet");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}

	@Bean
	Step nightStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("nightStep", jobRepository).tasklet((contribution, chunkContext) -> {
			log.info("This is the night tasklet");
			return RepeatStatus.FINISHED;
		}, transactionManager).build();
	}

	@Bean
	JobExecutionDecider decider() {
		return new TimeDecider();
	}

	@Bean
	Job job(JobRepository jobRepository, Step startStep, Step morningStep, Step nightStep, JobExecutionDecider decider) {
		return new JobBuilder("job", jobRepository).start(startStep).next(decider).from(decider).on("MORNING").to(morningStep)
				.from(decider).on("NIGHT").to(nightStep).from(morningStep).on("*").to(decider).from(decider).on("MORNING")
				.to(morningStep).from(decider).on("NIGHT").to(nightStep).end().
				listener(new JobExecutionListener() {
	                @Override
	                public void beforeJob(JobExecution jobExecution) {
	                	log.info(">> Job started");
	                }

	                @Override
	                public void afterJob(JobExecution jobExecution) {
	                	log.info(">> Job ended with status: " + jobExecution.getStatus());
	                }
	            }).build();
	}
}

