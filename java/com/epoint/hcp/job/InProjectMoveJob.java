package com.epoint.hcp.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@DisallowConcurrentExecution
public class InProjectMoveJob implements Job {

	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			log.info("办件数据迁移服务数据-----------------start");
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(new InProjectMoveCheck());
			
			executor.shutdown();
			while (true) {
				if (executor.isTerminated()) {
					log.info("======================ALL=========END======================");
					break;
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

}
