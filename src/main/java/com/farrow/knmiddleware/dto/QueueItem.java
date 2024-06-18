package com.farrow.knmiddleware.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueItem {
	private SourceSystem system;
	private DataType type;
}
