package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.DataType;


import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/accrual")

public class AccrualController extends AbstractSystemFileReceiverController<AccrualDTO> {
	@Override
	public ResponseEntity<String> receiveAs400PayLoad(HttpServletRequest req) throws IOException, SQLException {
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public DataType getType() {
		return DataType.ACCRUAL;
	}
}
