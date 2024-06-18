package com.farrow.knmiddleware;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.metadata.IIOMetadataNode;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farrow.knmiddleware.converters.InvoiceConverter;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.FlatFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;
import com.kn.services.kninvoiceheader.InvoiceHeaderInfo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;

import org.w3c.dom.Element;

import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

@SpringBootTest(properties= {"deploymentLevel=development"})
public class MappingTest {

	@Autowired FileObjectMappingUtility mapper;
	
	@Autowired InvoiceConverter invConverter;
	
	@Test
	void mapFile() throws IOException {
		String line = "CA13SALOGAR INV1234567890       ";
		FlatFileDefinition file = new FlatFileDefinition();
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(new Location[]{new Location("companyCode")},"Company Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("generatedFrom")},"Generated From",8,String.class));
		fields.add(new Field(new Location[]{new Location("itemNumber")},"Item #",20,String.class));
		file.setFields(fields);
		file.setRootType(InvoiceARHeader.class);
		try(InputStream stream = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));){
			List<Object> obj = mapper.getObjectFromSimpleFile(stream, file);
			InvoiceARHeader inv = (InvoiceARHeader) obj.get(0);
			InvoiceType kninv = invConverter.convertToKNObject(inv);
			byte[] xmlfile = invConverter.generateXml(kninv);
		    System.out.println(new String(xmlfile,StandardCharsets.UTF_8));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
	}

}
