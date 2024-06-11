package com.farrow.knmiddleware.dto;

public class ImageFile {
	private byte[] file;
	private String fileName;
	
	
	public ImageFile(){
		
	}
	
	public ImageFile(String fileName, byte[] file){
		this.file=file;
		this.fileName=fileName;
	}
	
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
