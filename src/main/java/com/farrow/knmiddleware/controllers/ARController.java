package com.farrow.knmiddleware.controllers;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/AR")
public class ARController {
	private static final Logger log = LogManager.getLogger(ARController.class);
	
	@Autowired private QueueDaoJdbc queueDao;
	
	@PostMapping("/as400test")
	public void receiveAs400ARPayLoadForTest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try(	InputStream is = req.getInputStream();
				InputStreamReader ireader = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(ireader);	){
			
			while(reader.ready()) {
				String line = reader.readLine();
				log.info(line);
			}
			
		}
		try(	OutputStream ros = resp.getOutputStream();
				OutputStreamWriter rosw = new OutputStreamWriter(ros);
				BufferedWriter writer = new BufferedWriter(rosw);){
			writer.append("OUT234234   23423423 234 234 23 423 423 42");
		}
	}
	
	@PostMapping("/as400")
	public void receiveAs400ARPayLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
		try(	InputStream is = req.getInputStream();){
			byte[] inputFile = IOUtils.toByteArray(is);
			QueueItem item = new QueueItem();
			item.setDataType(DataType.AR);
			item.setSourceSystem(SourceSystem.TSBAS400);
			QueueFile file = new QueueFile();
			file.setFile(inputFile);
			item.setInputFile(file);
			
			Integer id = queueDao.saveNewQueueItem(item);
			try(	OutputStream ros = resp.getOutputStream();
					OutputStreamWriter rosw = new OutputStreamWriter(ros);
					BufferedWriter writer = new BufferedWriter(rosw);){
				writer.append(""+id);
			}
		}
		
	}
	
}
