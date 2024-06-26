package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;

import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;

import jakarta.servlet.http.HttpServletRequest;

public abstract class AbstractSystemFileReceiverController<T> {
	private static final Logger log = LogManager.getLogger(AccrualController.class);
	
	@Autowired private QueueDaoJdbc queueDao;
	
	@PostMapping(value="/test")
	public ResponseEntity<String> receiveTestPayload(T object){
		return ResponseEntity.ok("Successfully mapped to Object");
	}
	
	
	@PostMapping(value = "/tsb")
	public ResponseEntity<String> receiveTSBPayload(HttpServletRequest req) throws  IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TSB);
			return ResponseEntity.ok(""+id);
		}	
	}
	
	@PostMapping("/as400")
	public ResponseEntity<String> receiveAs400PayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TSBAS400);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/tm")
	public ResponseEntity<String> receiveTMPayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TM);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/pl")
	public ResponseEntity<String> receivePLPayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.PL);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/flsi")
	public ResponseEntity<String> receiveFLSIPayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.FLSI);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	public abstract DataType getType();
	
	private Integer saveQueueItem(byte[] inputFile, SourceSystem sys) throws SQLException {
		QueueItem item = new QueueItem();
		item.setDataType(getType());
		item.setSourceSystem(sys);
		QueueFile file = new QueueFile();
		file.setFile(inputFile);
		item.setInputFile(file);
		
		Integer id = queueDao.saveNewQueueItem(item);
		return id;
	}
}
