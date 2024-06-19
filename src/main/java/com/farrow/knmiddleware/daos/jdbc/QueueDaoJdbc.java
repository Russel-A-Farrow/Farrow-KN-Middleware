package com.farrow.knmiddleware.daos.jdbc;

import java.time.LocalDateTime;

import com.farrow.knmiddleware.daos.AbstractFssbfdDaoJdbc;
import com.farrow.knmiddleware.dto.QueueItem;

public class QueueDaoJdbc extends AbstractFssbfdDaoJdbc{
	public void saveQueueItem(QueueItem item) {
		
	}
	
	public QueueItem lockNextConversionJob() {
		return null;
	}
	
	public QueueItem lockNextDeliveryJob() {
		return null;
	}
	
	public void saveConversionException(QueueItem item, Exception e) {
		
	}
	
	public void saveTransmissionException(QueueItem item, Exception e) {
		
	}
	
	public void cleanupObjectFiles(LocalDateTime threshold) {
		
	}
	
	public void cleanupInputFiles(LocalDateTime threshold) {
		
	}
	
	public void cleanupXmlFiles(LocalDateTime threshold) {
		
	}
	
	public void cleanupQueue(LocalDateTime threshold) {
		
	}
}
