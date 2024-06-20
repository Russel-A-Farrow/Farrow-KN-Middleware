package com.farrow.knmiddleware.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tsb")
public class TSBShipmentController {
	
	private final Logger logger = LogManager.getLogger(TSBShipmentController.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@PostMapping(value = "/shipments")
	public ResponseEntity<String> postShipmentDetails(@RequestBody ShipmentDetailsDTO tsbShipmentDetails) throws JsonProcessingException {
		logger.info("TSB Shipment Details: {}", mapper.writeValueAsString(tsbShipmentDetails));
		
		
		return ResponseEntity.ok("success");		
	}
}
