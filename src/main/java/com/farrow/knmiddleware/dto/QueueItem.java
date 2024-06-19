package com.farrow.knmiddleware.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueItem {
	private SourceSystem system;
	private DataType type;
	private QueueFile inputFile;
	private QueueFile objectFile;
	private QueueFile outputXml;
	private LocalDateTime created;
	private String conversionRunOn;
	private LocalDateTime conversionStarted;
	private LocalDateTime conversionCompleted;
	private String transmissionRunOn;
	private LocalDateTime transmissionStarted;
	private LocalDateTime transmissionCompleted;
}
