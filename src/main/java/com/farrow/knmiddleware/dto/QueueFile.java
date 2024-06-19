package com.farrow.knmiddleware.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueFile {
	private LocalDateTime created;
	private byte[] file;
	private int id;
}
