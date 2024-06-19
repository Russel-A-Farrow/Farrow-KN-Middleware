package com.farrow.knmiddleware.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/AR")
public class ARController {
	private static final Logger log = LogManager.getLogger(ARController.class);
	
	@PostMapping("/as400")
	public void receiveAs400ARPayLoad(HttpServletRequest req) throws IOException {
		try(	InputStream is = req.getInputStream();
				InputStreamReader ireader = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(ireader);	){
			
			while(reader.ready()) {
				String line = reader.readLine();
				log.info(line);
			}
		}
	}
	
}
