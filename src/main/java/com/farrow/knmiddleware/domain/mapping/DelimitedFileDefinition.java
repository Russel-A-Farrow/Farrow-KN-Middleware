package com.farrow.knmiddleware.domain.mapping;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DelimitedFileDefinition extends AbstractSimpleFile{

	private String delimiter;
}
