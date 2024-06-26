package com.farrow.knmiddleware.converters;

import java.util.List;

public interface KNObjectSubTypeConverter<T,F,S> {
	public T convertToKNObject(List<Object> inputs) throws Exception;
	
	public S convertToKNSubObject(F input) throws Exception;
}
