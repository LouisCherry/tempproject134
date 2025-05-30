package com.epoint.hcp.job;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.utils.log.LogUtil;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class HcpWaitEvaluateEightJob implements Job {
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			log.info("导入办件评价数据服务数据-----------------start");
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(new HcpWaitEvaluateEightJobCheck());
			
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
