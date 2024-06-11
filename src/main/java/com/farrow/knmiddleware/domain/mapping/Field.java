package com.farrow.knmiddleware.domain.mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Field {
	private List<Location> location;
	private String fieldName;
	private Integer size;
	private Class<Object> type;
	public Object parse(String value) {
		if(type.equals(String.class)) {
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
