package com.farrow.knmiddleware.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/AR")
public class ARController {
	private static final Logger log = LogManager.getLogger(ARController.class);
	
	@PostMapping("/as400")
	public void receiveAs400ARPayLoad(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
	
}
