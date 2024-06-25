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
	private Integer id;
	
	public Location (String prop, boolean isList) {
		this.prop=prop;
		this.isList=isList;
	}
	public Location(String prop) {
		this.prop=prop;
	}
}
