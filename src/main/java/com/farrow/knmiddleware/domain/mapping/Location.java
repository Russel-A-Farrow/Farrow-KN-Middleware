package com.farrow.knmiddleware.domain.mapping;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
	private String prop;
	private boolean isList = false;
	public Location(String prop) {
		this.prop=prop;
	}
}
