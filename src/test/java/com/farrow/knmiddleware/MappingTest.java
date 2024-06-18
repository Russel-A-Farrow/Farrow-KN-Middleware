package com.farrow.knmiddleware;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farrow.knmiddleware.converters.InvoiceConverter;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.FlatFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.FarrowDate;
import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.dto.InvoiceARLine;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

@SpringBootTest(properties= {"deploymentLevel=development"})
public class MappingTest {

	@Autowired FileObjectMappingUtility mapper;
	
	@Autowired InvoiceConverter invConverter;
	
	@Test
	void mapHeaderFile() throws IOException {
		String line = "CA13SALOGAR INV1234567890       ";
		FlatFileDefinition file = new FlatFileDefinition();
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
		fields.add(new Field(new Location[]{new Location("companyCode")},"Company Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("generatedFrom")},"Generated From",8,String.class));
		fields.add(new Field(new Location[]{new Location("debtorCode")},"Debtor Code",10,String.class));
		fields.add(new Field(new Location[]{new Location("itemType")},"Item Type",1,String.class));
		fields.add(new Field(new Location[]{new Location("itemNumber")},"Item No.",20,String.class));
		fields.add(new Field(new Location[]{new Location("itemDate")},"Item Date",10,FarrowDate.class));
		fields.add(new Field(new Location[]{new Location("itemDueDate")},"Due Date",10,FarrowDate.class));
		fields.add(new Field(new Location[]{new Location("itemCurrencyCode")},"Item Currency Code",3,String.class));
		fields.add(new Field(new Location[]{new Location("foreignCurrencyDecimialPlace")},"Foreign Currency Decimal Place",1,String.class));
		fields.add(new Field(new Location[]{new Location("localCurrencyDecimialPlace")},"Local Currency Decimal Place",1,String.class));
		fields.add(new Field(new Location[]{new Location("itemExchangeRate")},"Item Exchange Rate",18,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("lcAmount")},"LC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("fcAmount")},"FC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("vatLcAmount")},"VAT LC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("vatFcAmount")},"VAT FC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("cashInvoiceInd")},"Cash Invoice Indicator",1,String.class));
		fields.add(new Field(new Location[]{new Location("itemParticular")},"Item Particular",50,String.class));
		fields.add(new Field(new Location[]{new Location("clientId")},"Client ID",12,String.class));
		fields.add(new Field(new Location[]{new Location("itemParticular2")},"Item Particular 2",50,String.class));
		fields.add(new Field(new Location[]{new Location("itemParticular3")},"Item Particular 3",50,String.class));
		fields.add(new Field(new Location[]{new Location("itemParticular4")},"Item Particular 4",50,String.class));
		fields.add(new Field(new Location[]{new Location("vatSubtotalFcAmount")},"VAT Subtotal FC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("vatSubtotalVatableLcAmount")},"VAT Subtotal Vatable LC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("vatSubtotalLcAmount")},"VAT Subtotal LC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("vatSubtotalPercentage")},"VAT Subtotal Percentage",5,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("subtotalVatCode")},"VAT Subtotal VAT Code",2,String.class));
		fields.add(new Field(new Location[]{new Location("originalDocumentNumber")},"Original Document Number",20,String.class));

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
	
	@Test
	void mapLineFile() throws IOException {
		String line = "CA13SALOGAR INV1234567890       ";
		FlatFileDefinition file = new FlatFileDefinition();
		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
		fields.add(new Field(new Location[]{new Location("sequenceNumber")},"Sequence No.",6,String.class));
		fields.add(new Field(new Location[]{new Location("chargeCode")},"Charge Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("trackingNumber")},"Tracking No.",10,String.class));
		fields.add(new Field(new Location[]{new Location("profitCentre")},"Profit Centre",4,String.class));
		fields.add(new Field(new Location[]{new Location("filePeriod")},"File Period",6,String.class));
		fields.add(new Field(new Location[]{new Location("chargeCategory")},"Charge Category",3,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineLcAmount")},"Charge Line LC Amount",22,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineFcAmount")},"Charge Line FC Amount",22,String.class));
		fields.add(new Field(new Location[]{new Location("vatCode")},"VAT Code",2,String.class));
		fields.add(new Field(new Location[]{new Location("vatPercentage")},"VAT Percentage",5,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineVatLcAmount")},"Charge Line VAT LC Amount",22,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineVatFcAmount")},"Charge Line VAT FC Amount",22,String.class));
		fields.add(new Field(new Location[]{new Location("billingCompletedIndicator")},"Billing Completed Indicator",1,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineRemarks")},"Charge Line Remarks",50,String.class));
		fields.add(new Field(new Location[]{new Location("chargeLineParticular")},"Charge Line Particular",1000,String.class));
		fields.add(new Field(new Location[]{new Location("customerReference")},"Customer Reference",150,String.class));
		
		file.setFields(fields);
		file.setRootType(InvoiceARLine.class);
		try(InputStream stream = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));){
			List<Object> obj = mapper.getObjectFromSimpleFile(stream, file);
			InvoiceARLine inv = (InvoiceARLine) obj.get(0);
		    System.out.println(inv);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}	
	}

}
