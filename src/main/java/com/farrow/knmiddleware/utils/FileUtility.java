package com.farrow.knmiddleware.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtility {
	
	private final Logger logger = LogManager.getLogger(FileUtility.class);

	public File getTmpDir() {
		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "decoder" + File.separator + Thread.currentThread().getName() + "-" + Thread.currentThread().threadId());
		if (!file.isDirectory())
			file.mkdirs();
		return file;
	}

	/**
	 * Gets file from byte.
	 *
	 * @param bytes     the bytes
	 * @param extension the extension
	 * @return the file from byte
	 * @throws IOException the io exception
	 */
	public File getFileFromByte(byte[] bytes, String extension) throws IOException {
		File file = new File(getTmpDir().getAbsolutePath() + File.separator + Thread.currentThread().threadId() + extension);
		FileUtils.writeByteArrayToFile(file, bytes);
		return file;
	}

	/**
	 * Gets file from byte.
	 *
	 * @param bytes     the bytes
	 * @param fileName  the file name
	 * @param extension the extension
	 * @return the file from byte
	 * @throws IOException the io exception
	 */
	public File getFileFromByte(byte[] bytes, String fileName, String extension) throws IOException {
		File file = new File(getTmpDir().getAbsolutePath() + File.separator + fileName + Thread.currentThread().threadId() + extension);
		FileUtils.writeByteArrayToFile(file, bytes);
		return file;
	}

	public File storeFileToDisc(String fileName, byte[] fileContent, int counter) throws IOException {
		Objects.requireNonNull(fileName, "fileName cannot be null");
		try (InputStream stream = new ByteArrayInputStream(fileContent);) {

			fileName = sanitizeFileName(fileName);

			String filename = getTmpDir().getAbsolutePath() + File.separator + fileName;
			File docFile = new File(filename);
			if (docFile.exists()) {
				/*
				 * Put each attachment with the same name
				 * in their own sub folder to prevent issues when
				 * attachments have the same name.  Also preserves
				 * the attachment name in DMS image header.
				 */
				File subDir = new File(getTmpDir().getAbsolutePath() + File.separator + counter);
				subDir.mkdir();
				filename = subDir.getAbsolutePath() + File.separator + fileName;
				docFile = new File(filename);
			}

			try(FileOutputStream outFile = new FileOutputStream(docFile);) {
				IOUtils.copy(stream, outFile);
				outFile.flush();
				return docFile;
			}
		} catch (IOException e) {
			logger.warn("Exception occurred while storing file: {}", fileName, e);
			throw new IOException(e);
		}
	}

	/**
	 * Get byte from base 64 byte [ ].
	 *
	 * @param label the label
	 * @return the byte [ ]
	 */
	public static byte[] getByteFromBase64(String label) {
		return Base64.decodeBase64(label);
	}

	/**
	 * Gets file from byte.
	 *
	 * @param bytes the bytes
	 * @param file  the file
	 * @return the file from byte
	 * @throws IOException the io exception
	 */
	public File getFileFromByte(byte[] bytes, File file) throws IOException {
		FileUtils.writeByteArrayToFile(file, bytes);
		return file;
	}
	
	/**
	 * Gets file from byte.
	 *
	 * @param bytes the bytes
	 * @param file  the file
	 * @param off offset
	 * @return the file from byte
	 * @throws IOException the io exception
	 */
	public File getFileFromByte(byte[] bytes, File file, int off) throws IOException {
		FileUtils.writeByteArrayToFile(file, bytes, off, bytes.length - off);
		return file;
	}
	
	public static String sanitizeFileName(String filename) {
		if (filename == null)
		    return null;
		if (filename.contains("/")) 
		    filename = filename.replace("/", "_");
		if (filename.contains(":")) 
		    filename = filename.replace(":", "_");
		if (filename.contains("*")) 
		    filename = filename.replace("*", "_");
		if (filename.contains("?")) 
		    filename = filename.replace("?", "_");
		if (filename.contains("\"")) 
		    filename = filename.replace("\"", "_");
		if (filename.contains("<")) 
		    filename = filename.replace("<", "_");
		if (filename.contains(">")) 
		    filename = filename.replace(">", "_");
		if (filename.contains("|")) 
		    filename = filename.replace("|", "_");
		if (filename.contains("\\")) 
		    filename = filename.replace("\\", "_");
		if (filename.contains("&")) 
		    filename = filename.replace("&", "_");
		return filename;
	}
}
