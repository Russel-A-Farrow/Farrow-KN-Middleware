package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;

import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;

public abstract class AbstractSystemFileReceiverController<T> {
	private static final Logger log = LogManager.getLogger(AccrualController.class);
	
	@Autowired private QueueDaoJdbc queueDao;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exception(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@PostMapping(value="/test")
	public ResponseEntity<String> receiveTestPayload(@org.springframework.web.bind.annotation.RequestBody T object){
		return ResponseEntity.ok("Successfully mapped to Object");
	}
	
	
	@PostMapping(value = "/tsb")
	@RequestBody(content=@Content(schema=@Schema(implementation=String.class)))
	public ResponseEntity<String> receiveTSBPayload(HttpServletRequest req) throws  IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TSB);
			return ResponseEntity.ok(""+id);
		}	
	}
	
	@PostMapping("/as400")
	@RequestBody(content=@Content(schema=@Schema(implementation=String.class)))
	public ResponseEntity<String> receiveAs400PayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TSBAS400);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/tm")
	@RequestBody(content=@Content(schema=@Schema(implementation=String.class)))
	public ResponseEntity<String> receiveTMPayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TM);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/pl")
	@RequestBody(content=@Content(schema=@Schema(implementation=String.class)))
	public ResponseEntity<String> receivePLPayLoad(HttpServletRequest req) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.PL);
			return ResponseEntity.ok(""+id);
		}
		
	}
	
	@PostMapping("/flsi")
	@RequestBody(content=@Content(schema=@Schema(implementation=String.class)))
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
