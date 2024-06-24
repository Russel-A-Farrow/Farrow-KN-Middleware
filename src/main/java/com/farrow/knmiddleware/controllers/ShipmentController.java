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

import com.farrow.knmiddleware.converters.ShipmentDetailsConverter;
import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.xml.bind.JAXBException;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {
	
	private final Logger logger = LogManager.getLogger(ShipmentController.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ShipmentDetailsConverter shipmentDetailsConverter;
	
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
}
