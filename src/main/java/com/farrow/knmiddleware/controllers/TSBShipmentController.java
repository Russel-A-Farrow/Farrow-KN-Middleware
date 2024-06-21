package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.converters.ShipmentDetailsConverter;
import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.xml.bind.JAXBException;

@RestController
@RequestMapping("/tsb")
public class TSBShipmentController {
	
	private final Logger logger = LogManager.getLogger(TSBShipmentController.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private ShipmentDetailsConverter shipmentDetailsConverter;
	
	@PostMapping(value = "/shipments")
	public ResponseEntity<String> postShipmentDetails(@RequestBody ShipmentDetailsDTO tsbShipmentDetails) throws JAXBException, IOException {
		logger.info("TSB Shipment Details: {}", mapper.writeValueAsString(tsbShipmentDetails));
		
		logger.info(new String(shipmentDetailsConverter.generateXml(shipmentDetailsConverter.convertToKNObject(tsbShipmentDetails)),
				StandardCharsets.UTF_8));
		
		return ResponseEntity.ok("success");		
	}
}
