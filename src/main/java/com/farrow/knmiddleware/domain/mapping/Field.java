package com.farrow.knmiddleware.domain.mapping;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Field {
	private List<String> location;
	private String fieldName;
	private Integer size;
	private Class<Object> type;
	
	
}
