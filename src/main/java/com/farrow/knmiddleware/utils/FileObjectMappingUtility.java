package com.farrow.knmiddleware.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.domain.mapping.AbstractSimpleFile;
import com.farrow.knmiddleware.domain.mapping.ComplexFileDefinition;
import com.farrow.knmiddleware.domain.mapping.DelimitedFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.FlatFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FileObjectMappingUtility {
	
	public static final String DO_NOT_MAP = "^DONOTMAP^";
	@Autowired private ObjectMapper mapper;
	
	public List<Object> getObjectFromComplexFile(InputStream fileIs, ComplexFileDefinition file) throws Exception {
		List<Object> list = new ArrayList<>();
		Map<String,Object> map = new HashMap<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileIs));
		String line = null;
		while ((line = reader.readLine())!=null) {
			String type = line.substring(0, file.getTypeFieldSize());
			if(list.size()>0 &&file.getTypeResetValue()!=null && type==file.getTypeResetValue()) {
				list.add(mapper.convertValue(map, file.getRootType()));
				map = new HashMap<>();
			}
			Object value = mapLine(line,file.getRowTypes().get(type));
			addToMapLocation(value, file.getRowTypes().get(type).getLocation(), map);
			
		}
		list.add(mapper.convertValue(map, file.getRootType()));
		return list;
	}
	
	public List<Object> getObjectFromSimpleFile(InputStream fileIs, AbstractSimpleFile file) throws Exception {
		List<Object> list = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileIs));
		String line = null;
		while ((line = reader.readLine())!=null) {
			Object value = mapLine(line,file);
			list.add(mapper.convertValue(value, file.getRootType()));
		}
		return list;
	}
	
	private void addToMapLocation(Object value, List<Location> location, Map<String,Object> map) {
		if(location==null || location.isEmpty()) {
			throw new UnsupportedOperationException("Location not defined");
		}
		if(!Objects.equals(location.get(0).getProp(), DO_NOT_MAP)) {
			Map<String,Object> currentMap = map;
			for(int i=0;i<location.size();i++) {
				String prop=location.get(i).getProp();
				boolean notList = !location.get(i).isList();
				if((i+1)==location.size()) {
					//Add property
					if(notList) {
						currentMap.put(prop, value);
					}
					else if(!notList && currentMap.get(prop)!=null && currentMap.get(prop) instanceof List) {
						((List<Object>)currentMap.get(prop)).add(value);
					}
					else if (!notList && currentMap.get(prop)==null) {
						List<Object> list = new ArrayList<>();
						list.add(value);
						currentMap.put(prop, list);
					}
					else {
						throw new UnsupportedOperationException("List not found");
					}
				}
				else if(notList && currentMap.get(prop)!=null && currentMap.get(prop) instanceof Map) {
					currentMap = currentMap.getClass().cast(currentMap.get(prop));
				}
				else if(notList && currentMap.get(prop)==null) {
					Map<String,Object> newMap = new HashMap<String,Object>();
					currentMap.put(prop, newMap);
					currentMap=newMap;
					
				}
				else if(!notList) {
					throw new UnsupportedOperationException("List not suppoted in middle locations");
				}
			}
		}
		
	}
	
	private Object mapDelimitedLine(String line, DelimitedFileDefinition def) throws Exception{		
		Map<String,Object> map = new HashMap<>();
		List<String> tempFields =  Arrays.asList(line.split(def.getDelimiter()));
		if(def.getFields().size()!=tempFields.size()) {
			throw new UnsupportedOperationException("Definition field size doesn't match actual field size");
		}
		for(int i=0;i<def.getFields().size();i++) {
			Field field = def.getFields().get(i);
			Object value = field.parse(tempFields.get(i));
			addToMapLocation(value,field.getLocation(),map);
		}
		return mapper.convertValue(map, def.getRootType());
	}
	


	private Object mapFlatFileLine(String line, FlatFileDefinition def) throws Exception{
		Map<String,Object> map = new HashMap<>();
		Integer startPos = 0;
		for(int i=0;i<def.getFields().size();i++) {
			Field field = def.getFields().get(i);
			Object value = field.parse(line.substring(startPos, startPos+field.getSize()));
			startPos+=field.getSize();
			addToMapLocation(value,field.getLocation(),map);
		}
		return mapper.convertValue(map, def.getRootType());
	}
	
	private Object mapLine(String line, AbstractSimpleFile def) throws Exception {
		if(def instanceof DelimitedFileDefinition ddef) {
			return mapDelimitedLine(line,ddef);
		}
		else if(def instanceof FlatFileDefinition fdef) {
			return mapFlatFileLine(line,fdef);
		}
		else {
			throw new UnsupportedOperationException("Invalid file definition");
		}
	}
}
