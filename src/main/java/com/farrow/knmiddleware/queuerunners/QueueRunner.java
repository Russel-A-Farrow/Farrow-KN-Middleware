package com.farrow.knmiddleware.queuerunners;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.farrow.knmiddleware.daos.jdbc.QueueDaoJdbc;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;
import com.farrow.knmiddleware.utils.SFTPUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Session;

public abstract class QueueRunner {
	
	@Autowired protected ObjectMapper mapper;
	
	@Autowired protected FileObjectMappingUtility fileMapper;
	
	@Autowired protected QueueDaoJdbc queueDao;
	
	@Value("${knsftphost}")	protected String sftpHost;
	@Value("${knsftpuser}")	protected String sftpUser;
	@Value("${knsftppass}")	protected String sftpPass;
	@Value("${knsftpport}")	protected String sftpPort;
	
	protected static final DateTimeFormatter FILEDATEFORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	
	public abstract DataType getType();
	
	public abstract void convertData(QueueItem item) throws Exception;
	
	public abstract void transmitData(QueueItem item) throws Exception;
	
	protected void genericTransmitData(QueueItem item, String location) throws Exception {
		Session sftp = null;
		try(InputStream is = new ByteArrayInputStream(item.getOutputXml().getFile())){
			sftp = SFTPUtility.getSession(sftpHost,sftpUser, sftpPass, Integer.parseInt(sftpPort));
			String filename = item.getSourceSystem().toString()+item.getDataType().toString()+LocalDateTime.now().format(FILEDATEFORMAT)+(item.getId()%100);
			SFTPUtility.uploadFile(sftp, is, location, filename);
		}
		finally {
			if(sftp!=null) {
				SFTPUtility.disconnectSession(sftp);
			}
		}
	}
}
