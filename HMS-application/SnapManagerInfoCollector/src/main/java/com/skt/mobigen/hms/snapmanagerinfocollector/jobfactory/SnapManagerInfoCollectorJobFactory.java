package com.skt.mobigen.hms.snapmanagerinfocollector.jobfactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.mobigen.hms.snapmanagerinfocollector.context.Context;
import com.skt.mobigen.hms.snapmanagerinfocollector.service.SnapManagerPluginInfoCollector;
import com.skt.mobigen.hms.snapmanagerinfocollector.service.SnapManagerTaskInfoCollector;

public class SnapManagerInfoCollectorJobFactory {
	private Logger logger = LoggerFactory.getLogger(SnapManagerInfoCollectorJobFactory.class);
	private Context context;

	public void setContext(Context context) {
		this.context = context;
	}

	public void jobfactory() {
		ExecutorService pool = Executors.newCachedThreadPool();
		try {
			while (true) {
				Future<String> plugin_future = pool.submit(new SnapManagerPluginInfoCollector(context));
				Future<String> task_future = pool.submit(new SnapManagerTaskInfoCollector(context));
				try {
					if (!plugin_future.get(5, TimeUnit.MINUTES).equals("completed"))
						throw new TimeoutException(plugin_future.get());
					if (!task_future.get(5, TimeUnit.MINUTES).equals("completed"))
						throw new TimeoutException(task_future.get());
				} catch (TimeoutException te) {
					logger.error(ExceptionUtils.getStackTrace(te));
					Thread.sleep(30000);
					continue;
				}

				Thread.sleep(Long.parseLong(context.getProcess_interval_time()) * 1000);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			// logger.error(ExceptionUtils.getMessage(e));
		}
	}
}
