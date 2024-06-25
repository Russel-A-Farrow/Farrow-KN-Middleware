package com.farrow.knmiddleware.domain.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComplexFileDefinition{
	private Map<String,AbstractSimpleFile> rowTypes;
	private Class<?> rootType;
	private Integer typeFieldSize;
	private String typeResetValue;
	public void addRowType(String string, AbstractSimpleFile file) {
		if(rowTypes==null) {
			rowTypes = new HashMap<String,AbstractSimpleFile>();
		}
		rowTypes.put(string, file);	
	}
}
