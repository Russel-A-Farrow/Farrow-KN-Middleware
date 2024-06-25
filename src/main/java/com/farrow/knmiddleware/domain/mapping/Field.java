package com.farrow.knmiddleware.domain.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field {
	private List<Location> location;
	private String fieldName;
	private Integer size;
	private Integer decimalPlaces;
	private Class<?> type;
	
	
	public Field (Location[] locations, String fieldName, Integer size, Class<?>type) {
		this(Arrays.asList(locations),fieldName,size,null,type);
	}
	
	public Field (Location[] locations, String fieldName, Integer size, Integer decimal, Class<?>type) {
		this(Arrays.asList(locations),fieldName,size,decimal,type);
	}
	
	public Object parse(String value)  throws Exception {
		if(decimalPlaces!=null) {
			value = value.substring(0,size-decimalPlaces) + '.' + value.substring(size-decimalPlaces);
		}
		value=StringUtils.trimToNull(value);
		if(value==null) {
			return null;
		}
		else if(type.equals(String.class)) {
			return value;
		}
		else if(type.equals(Integer.class)) {
			return Integer.parseInt(value);
		}
		else if(type.equals(Boolean.class)) {
			return Boolean.parseBoolean(value);
		}
		else if(type.equals(Double.class)){
			return Double.parseDouble(value);
		}
		else if(type.equals(BigDecimal.class)){
			return new BigDecimal(value);
		}
		else if(type.equals(XMLGregorianCalendar.class)) {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(value.replaceAll("\\/", "-"));
		}
		else {
			try {
				Method parse = type.getDeclaredMethod("parse", String.class);
				return parse.invoke(null, value);
			}
			catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new UnsupportedOperationException("No parse method found, or cannot run on "+type.getClass().getName());
			}
		}
	}
	
}
