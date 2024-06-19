package com.farrow.knmiddleware.queuerunners;

import org.springframework.beans.factory.annotation.Autowired;

import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;

public abstract class QueueRunner {
	
	@Autowired protected FileObjectMappingUtility fileMapper;
	
	public abstract DataType getType();
	
	public abstract void convertData(QueueItem item);
	
	public abstract void transmitData(QueueItem item);
}
