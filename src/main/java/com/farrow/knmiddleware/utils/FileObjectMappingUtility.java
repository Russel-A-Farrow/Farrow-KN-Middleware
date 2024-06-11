package com.farrow.knmiddleware.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;

import com.farrow.knmiddleware.domain.mapping.ComplexFileDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileObjectMappingUtility {
	@Autowired private ObjectMapper mapper;
	
	private Object getObjectFromFile(InputStream fileIs, ComplexFileDefinition file) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileIs));
		return null;
	}
}
