package com.farrow.knmiddleware.controllers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.converters.ShipmentDetailsConverter;
import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBException;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {
	
	private final Logger logger = LogManager.getLogger(ShipmentController.class);
	
	@Autowired private ObjectMapper mapper;
	
	@Autowired private QueueDaoJdbc queueDao;
	
	@PostMapping(value = "/tsb")
	public ResponseEntity<String> postShipmentDetails(@RequestBody ShipmentDetailsDTO tsbShipmentDetails) throws JAXBException, IOException, SQLException {
		byte[] inputFile = mapper.writeValueAsBytes(tsbShipmentDetails);
		QueueItem item = new QueueItem();
		item.setDataType(DataType.SHIPMENT);
		item.setSourceSystem(SourceSystem.TSB);
		QueueFile file = new QueueFile();
		file.setFile(inputFile);
		item.setInputFile(file);
		
		Integer id = queueDao.saveNewQueueItem(item);
		return ResponseEntity.ok("success-"+id);		
	}
	
	@PostMapping("/tm")
	public void receiveTMARPayLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.TM);
			try(	OutputStream ros = resp.getOutputStream();
					OutputStreamWriter rosw = new OutputStreamWriter(ros);
					BufferedWriter writer = new BufferedWriter(rosw);){
				writer.append(""+id);
			}
		}
		
	}
	
	@PostMapping("/pl")
	public void receivePLARPayLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.PL);
			try(	OutputStream ros = resp.getOutputStream();
					OutputStreamWriter rosw = new OutputStreamWriter(ros);
					BufferedWriter writer = new BufferedWriter(rosw);){
				writer.append(""+id);
			}
		}
		
	}
	
	@PostMapping("/flsi")
	public void receiveFLSIARPayLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			Integer id = saveQueueItem(inputFile,SourceSystem.FLSI);
			try(	OutputStream ros = resp.getOutputStream();
					OutputStreamWriter rosw = new OutputStreamWriter(ros);
					BufferedWriter writer = new BufferedWriter(rosw);){
				writer.append(""+id);
			}
		}
		
	}
	
	private Integer saveQueueItem(byte[] inputFile, SourceSystem sys) throws SQLException {
		QueueItem item = new QueueItem();
		item.setDataType(DataType.SHIPMENT);
		item.setSourceSystem(sys);
		QueueFile file = new QueueFile();
		file.setFile(inputFile);
		item.setInputFile(file);
		
		Integer id = queueDao.saveNewQueueItem(item);
		return id;
	}
	
}
