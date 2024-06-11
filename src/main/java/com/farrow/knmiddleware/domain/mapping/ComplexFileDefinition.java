package com.farrow.knmiddleware.domain.mapping;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ComplexFileDefinition {
	private Map<String,AbstractSimpleFile> rowTypes;
	private Class<Object> rootType;
	private Integer typeFieldNumber;
}
