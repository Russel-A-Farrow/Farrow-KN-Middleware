package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.xml.bind.JAXBException;

@RestController
@RequestMapping("/Accrual")
public class AccrualController {
	private static final Logger log = LogManager.getLogger(AccrualController.class);
	
	@Autowired private QueueDaoJdbc queueDao;
	
	@Autowired private ObjectMapper mapper;
	
	@PostMapping(value = "/tsb")
	public ResponseEntity<String> postAccrualDetails(@RequestBody AccrualDTO accrual) throws JAXBException, IOException, SQLException {
		byte[] inputFile = mapper.writeValueAsBytes(accrual);
		QueueItem item = new QueueItem();
		item.setDataType(DataType.ACCRUAL);
		item.setSourceSystem(SourceSystem.TSB);
		QueueFile file = new QueueFile();
		file.setFile(inputFile);
		item.setInputFile(file);
		
		Integer id = queueDao.saveNewQueueItem(item);
		return ResponseEntity.ok("success-"+id);		
	}
}
