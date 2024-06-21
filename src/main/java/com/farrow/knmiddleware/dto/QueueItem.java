package com.farrow.knmiddleware.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueItem {
	private Integer id;
	private SourceSystem sourceSystem;
	private DataType dataType;
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


/*

	CREATE SCHEMA DEVKNMIDDLEWARE
	
	
	
	CREATE TABLE devknmiddleware.queueInputFile (
		id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		textcontent clob(524288000) NOT NULL,
		created timestamp NOT NULL DEFAULT current_timestamp		
	)
	
	
	CREATE TABLE devknmiddleware.queueObjectFile (
		id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		textcontent clob(524288000) NOT NULL,
		created timestamp NOT NULL DEFAULT current_timestamp		
	)
	
	CREATE TABLE devknmiddleware.queueOutputFile (
		id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		textcontent clob(524288000) NOT NULL,
		created timestamp NOT NULL DEFAULT current_timestamp		
	)
	
	CREATE TABLE devknmiddleware.queue(
		id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		sourceSystem varchar(10) NOT null,
		dataType varchar(25) NOT NULL,
		inputFileId INT,
		objectFileId INT,
		outputXmlId INT,
		created TIMESTAMP NOT NULL DEFAULT current_timestamp,
		conversionRunOn varchar(100),
		conversionStarted timestamp,
		conversionCompleted timestamp,
		transmissionRunOn varchar(100),
		transmissionStarted timestamp,
		transmissionCompleted timestamp
	)


	CREATE TABLE devknmiddleware.queueExceptions (
		id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
		queueid int NOT NULL,
		exceptionRunType varchar(20) NOT null,
		errorMessage varchar(1024) NOT null,
		stacktrace varchar(31000)
	)
*/