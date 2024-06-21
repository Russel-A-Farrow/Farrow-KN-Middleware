package com.farrow.knmiddleware.queuerunners;

import org.springframework.beans.factory.annotation.Autowired;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class QueueRunner {
	
	@Autowired protected ObjectMapper mapper;
	
	@Autowired protected FileObjectMappingUtility fileMapper;
	
	@Autowired protected QueueDaoJdbc queueDao;
	
	public abstract DataType getType();
	
	public abstract void convertData(QueueItem item) throws Exception;
	
	public abstract void transmitData(QueueItem item) throws Exception;
}
