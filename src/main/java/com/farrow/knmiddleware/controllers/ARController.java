package com.farrow.knmiddleware.controllers;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.InvoiceARHeader;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/AR")
public class ARController extends  AbstractSystemFileReceiverController<InvoiceARHeader> {
	@Override
	public ResponseEntity<String> receiveTSBPayload(HttpServletRequest req) throws  IOException, SQLException{
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public DataType getType() {
		return DataType.AR;
	}

}
