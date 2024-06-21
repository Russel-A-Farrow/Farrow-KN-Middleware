package com.farrow.knmiddleware.queuerunners;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.farrow.knmiddleware.converters.InvoiceConverter;
import com.farrow.knmiddleware.domain.mapping.ComplexFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.FlatFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.dto.InvoiceARLine;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.farrow.knmiddleware.exceptions.UnsupportedSourceSystemException;
import com.farrow.knmiddleware.utils.FileObjectMappingUtility;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

public class ARInvoiceQueueRunner extends QueueRunner {

	@Autowired private InvoiceConverter invoiceConverter;
	
	private static final ComplexFileDefinition AS400_MAP ;
	static {
		FlatFileDefinition file = new FlatFileDefinition();
		List<Field> fields = new ArrayList<>();
		fields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
		fields.add(new Field(new Location[]{new Location("companyCode")},"Company Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("generatedFrom")},"Generated From",8,String.class));
		fields.add(new Field(new Location[]{new Location("debtorCode")},"Debtor Code",10,String.class));
		fields.add(new Field(new Location[]{new Location("itemType")},"Item Type",1,String.class));
		fields.add(new Field(new Location[]{new Location("itemNumber")},"Item No.",20,String.class));
		fields.add(new Field(new Location[]{new Location("itemDate")},"Item Date",10,XMLGregorianCalendar.class));
		fields.add(new Field(new Location[]{new Location("itemDueDate")},"Due Date",10,XMLGregorianCalendar.class));
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
		
		FlatFileDefinition dtlfile = new FlatFileDefinition();
		List<Field> dtlfields = new ArrayList<>();
		dtlfields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
		dtlfields.add(new Field(new Location[]{new Location("sequenceNumber")},"Sequence No.",6,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeCode")},"Charge Code",4,String.class));
		dtlfields.add(new Field(new Location[]{new Location("trackingNumber")},"Tracking No.",10,String.class));
		dtlfields.add(new Field(new Location[]{new Location("profitCentre")},"Profit Centre",4,String.class));
		dtlfields.add(new Field(new Location[]{new Location("filePeriod")},"File Period",6,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeCategory")},"Charge Category",3,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineLcAmount")},"Charge Line LC Amount",22,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineFcAmount")},"Charge Line FC Amount",22,String.class));
		dtlfields.add(new Field(new Location[]{new Location("vatCode")},"VAT Code",2,String.class));
		dtlfields.add(new Field(new Location[]{new Location("vatPercentage")},"VAT Percentage",5,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineVatLcAmount")},"Charge Line VAT LC Amount",22,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineVatFcAmount")},"Charge Line VAT FC Amount",22,String.class));
		dtlfields.add(new Field(new Location[]{new Location("billingCompletedIndicator")},"Billing Completed Indicator",1,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineRemarks")},"Charge Line Remarks",50,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineParticular")},"Charge Line Particular",1000,String.class));
		dtlfields.add(new Field(new Location[]{new Location("customerReference")},"Customer Reference",150,String.class));
		dtlfile.setFields(dtlfields);
		dtlfile.setRootType(InvoiceARLine.class);
		dtlfile.setLocation(Arrays.asList(new Location("invoiceARLines")));
		
		
		AS400_MAP = new ComplexFileDefinition();
		AS400_MAP.addRowType("HDR",file);
		AS400_MAP.addRowType("DTL", dtlfile);
		AS400_MAP.setTypeResetValue("HDR");
		AS400_MAP.setTypeFieldSize(3);
		
	}
	
	@Override
	public DataType getType() {
		return DataType.AR;
	}

	@Override
	public void convertData(QueueItem item) throws Exception {
		if(SourceSystem.TSB_AS400.equals(item.getSourceSystem())) {
			try(ByteArrayInputStream bis = new ByteArrayInputStream(item.getInputFile().getFile())){
				List<Object> invoiceHeaders = fileMapper.getObjectFromComplexFile(bis, AS400_MAP);
				if(invoiceHeaders.size()>1) {
					throw new Exception("Too many invoices in record");
				}
				InvoiceARHeader arHeader = (InvoiceARHeader)invoiceHeaders.get(0);
				byte[] objectFile = mapper.writeValueAsBytes(arHeader);
				QueueFile objectQueueFile = new QueueFile();
				objectQueueFile.setFile(objectFile);
				item.setObjectFile(objectQueueFile);
				queueDao.saveObjectFile(item.getId(),item.getObjectFile());
				InvoiceType invoice = invoiceConverter.convertToKNObject(arHeader);
				byte[] xmlFile = invoiceConverter.generateXml(invoice);
				QueueFile xmlQueueFile = new QueueFile();
				xmlQueueFile.setFile(xmlFile);
				item.setOutputXml(xmlQueueFile);
				queueDao.saveOutputFile(item.getId(),item.getOutputXml());
			}
			
		}
		else if(SourceSystem.TM.equals(item.getSourceSystem())) {
			//TODO
		}
		else if(SourceSystem.PL.equals(item.getSourceSystem())) {
			//TODO
		}
		else {
			throw new UnsupportedSourceSystemException(item.getSourceSystem()+" is unsupported for "+item.getDataType());
		}
	}

	@Override
	public void transmitData(QueueItem item) throws Exception {
		// TODO Auto-generated method stub

	}

}
