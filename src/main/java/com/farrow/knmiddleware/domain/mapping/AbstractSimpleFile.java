package com.farrow.knmiddleware.domain.mapping;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractSimpleFile {	
	private List<Location> location;
	private Class<?> rootType;
	private List<Field> fields;
}
