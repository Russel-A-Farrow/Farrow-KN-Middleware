package com.farrow.knmiddleware.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.queuerunners.QueueRunner;

@Component
public class ScheduledQueueRunner implements ApplicationRunner {
	
	private static final Logger log = LogManager.getLogger(ScheduledQueueRunner.class);
	
	@Autowired private QueueDaoJdbc queueDao;
	
	private Map<DataType,QueueRunner> queueRunners = new HashMap<>();
	
	@Autowired
	public void setQueueRunners(List<QueueRunner> runners) {
		for(QueueRunner runner:runners) {
			queueRunners.put(runner.getType(), runner);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Runnable processConversions = () -> processConversions();
		ScheduledExecutorService queueProcessing = Executors.newScheduledThreadPool(1, new CustomizableThreadFactory("queue-processor-thread-"));
		queueProcessing.scheduleWithFixedDelay(processConversions, 1,1, TimeUnit.MINUTES);
		
		Runnable processTransmissions = () -> processTransmissions();
		ScheduledExecutorService queueProcessingTransmissions = Executors.newScheduledThreadPool(1, new CustomizableThreadFactory("queue-processor-thread-"));
		queueProcessingTransmissions.scheduleWithFixedDelay(processTransmissions, 1,1, TimeUnit.MINUTES);
		
	}

	private void processConversions()  {
		
		try {
			QueueItem item;
			while ((item = queueDao.lockNextConversionJob())!=null) {
				try {
					queueRunners.get(item.getDataType()).convertData(item);
					queueDao.completeConversion(item.getId());
				}
				catch(Exception e) {
					queueDao.saveConversionException(item.getId(), e);
				}
			}
		}
		catch(Exception e) {
			log.error(e);
		}
	}

	private void processTransmissions() {
		try {
			QueueItem item;
			while ((item = queueDao.lockNextDeliveryJob())!=null) {
				try {
					queueRunners.get(item.getDataType()).transmitData(item);
					queueDao.completeTransmission(item.getId());
				}
				catch(Exception e) {
					queueDao.saveTransmissionException(item.getId(), e);
				}
			}
		}
		catch(Exception e) {
			log.error(e);
		}
	}
}
