package com.farrow.knmiddleware.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public abstract class KNObjectConverter<T,F> {
	
	protected JAXBContext context;
	protected Marshaller marshaller;
	
	public KNObjectConverter(JAXBContext context) throws JAXBException {
		this.context = context;
		this.marshaller = context.createMarshaller();
		//For Debugging
		if("development".equals(System.getProperty("deploymentLevel"))) {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		}
	}
	
	public abstract T convertToKNObject(F input) throws Exception;
	
	public abstract JAXBElement<T> createObject(T input);
	
	
	public byte[] generateXml(T input) throws JAXBException, IOException{
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
			JAXBElement<T> jAXBElement = createObject(input);
			marshaller.marshal(jAXBElement, bos);
		    return bos.toByteArray();
		}
	}
	
}
