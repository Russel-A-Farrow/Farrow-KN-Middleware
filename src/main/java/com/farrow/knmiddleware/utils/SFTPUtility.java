package com.farrow.knmiddleware.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SFTPUtility {
	
	private static Logger log = LogManager.getLogger(SFTPUtility.class);

	public static Session getSession(String hostName, String userName, String password, int port) throws JSchException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(userName, hostName, port);
		session.setPassword(password);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		return session;
	}

	public static void disconnectSession(Session session) {
		session.disconnect();
	}
	
	public static void uploadFile(Session session,InputStream file, String destDirectory,String destFileName) throws JSchException, SftpException {

		ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
		log.info("Connection Established");
		channelSftp.connect();
		channelSftp.put(file, destDirectory+"/"+destFileName);
		channelSftp.exit();

	}
	
	public static void uploadFile(Session session,File file, String destDirectory,String destFileName) throws JSchException, SftpException, IOException  {
		try(FileInputStream fis = new FileInputStream(file)){
			uploadFile(session,fis,destDirectory,destFileName);
		}
	}
	
	public static void uploadFile(Session session,File file, String destDirectory) throws JSchException, SftpException, IOException  {
		uploadFile(session,file,destDirectory,file.getName());
	}
	
	public static void uploadFile(String hostName, String userName, String password, int port,File file, String destDirectory) throws JSchException, SftpException, IOException  {
		Session session = getSession(hostName,userName,password,port);
		uploadFile(session,file,destDirectory);
		disconnectSession(session);
	}
	public static void uploadFile(String hostName, String userName, String password, int port,File file, String destDirectory, String filename) throws JSchException, SftpException, IOException  {
		Session session = getSession(hostName,userName,password,port);
		uploadFile(session,file,destDirectory,filename);
		disconnectSession(session);
	}
	

}
