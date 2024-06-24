package com.farrow.knmiddleware;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farrow.knmiddleware.converters.InvoiceConverter;
import com.farrow.knmiddleware.domain.mapping.ComplexFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.FlatFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.dto.InvoiceARLine;
import com.farrow.knmiddleware.queuerunners.ARInvoiceQueueRunner;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

@SpringBootTest(properties= {"deploymentLevel=development"})
public class MappingTest {

	@Autowired FileObjectMappingUtility mapper;
	
	@Autowired InvoiceConverter invConverter;
	
	@Test
	void mapHeaderFile() throws IOException {
		
		String lines = "HDRCA13TRADSAR 1234567890IITEMNUMBER          2024-06-242024-06-29CAD110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000YNOTEITEM                                          CLIENTID    ITEMPARTICULAR2                                   ITEMPARTICULAR3                                   ITEMPARTICULAR4                                   00000000000000000000000000000000000000000000000000000000000000000000623ADORIGINAL123456789012\n"
				+ "DTL000001ABCD1234567890PR13240624CAT00000000000000000000000000000000000000000000VA0995200000000000000000000000000000000000000000000VA0995200000000000000000000000000000000000000000000YNOTE                                              PARTICULARS1000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         CUSTOMERREFERENCE                                                                                                                                     \n"
				+ "DTL000002ABCD1234567890PR13240624CAT00000000000000000000000000000000000000000000VA0995200000000000000000000000000000000000000000000VA0995200000000000000000000000000000000000000000000YNOTE                                              PARTICULARS1000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         CUSTOMERREFERENCE                                                                                                                                     ";
		ComplexFileDefinition file = ARInvoiceQueueRunner.AS400_MAP;


		try(InputStream stream = new ByteArrayInputStream(lines.getBytes(StandardCharsets.UTF_8));){
			List<Object> obj = mapper.getObjectFromComplexFile(stream, file);
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
