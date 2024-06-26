package com.farrow.knmiddleware.queuerunners;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.converters.InvoiceConverter;
import com.farrow.knmiddleware.domain.mapping.ComplexFileDefinition;
import com.farrow.knmiddleware.domain.mapping.DelimitedFileDefinition;
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
import com.farrow.knmiddleware.utils.SFTPUtility;
import com.jcraft.jsch.Session;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

@Component
public class ARInvoiceQueueRunner extends QueueRunner {

	@Autowired private InvoiceConverter invoiceConverter;
	
	public static final ComplexFileDefinition AS400_MAP ;
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
		fields.add(new Field(new Location[]{new Location("itemExchangeRate")},"Item Exchange Rate",18,10,BigDecimal.class));
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
		fields.add(new Field(new Location[]{new Location("vatSubtotalPercentage")},"VAT Subtotal Percentage",5,2,BigDecimal.class));
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
		for(int i = 0; i<2;i++) {
			dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("vatCode")},"VAT Code",2,String.class));
			dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("vatPercentage")},"VAT Percentage",5,2,String.class));
			dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("chargeLineVatLcAmount")},"Charge Line VAT LC Amount",22,String.class));
			dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("chargeLineVatFcAmount")},"Charge Line VAT FC Amount",22,String.class));
		}
		dtlfields.add(new Field(new Location[]{new Location("billingCompletedIndicator")},"Billing Completed Indicator",1,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineRemarks")},"Charge Line Remarks",50,String.class));
		dtlfields.add(new Field(new Location[]{new Location("chargeLineParticular")},"Charge Line Particular",1000,String.class));
		dtlfields.add(new Field(new Location[]{new Location("customerReference")},"Customer Reference",150,String.class));
		dtlfile.setFields(dtlfields);
		dtlfile.setRootType(InvoiceARLine.class);
		dtlfile.setLocation(Arrays.asList(new Location("invoiceARLines",true)));
		
		
		AS400_MAP = new ComplexFileDefinition();
		AS400_MAP.setRootType(InvoiceARHeader.class);
		AS400_MAP.addRowType("HDR",file);
		AS400_MAP.addRowType("DTL", dtlfile);
		AS400_MAP.setTypeResetValue("HDR");
		AS400_MAP.setTypeFieldSize(3);
		
	}
	
	public static final ComplexFileDefinition TMPL_MAP;
	
	static {
			DelimitedFileDefinition file = new DelimitedFileDefinition();
			List<Field> fields = new ArrayList<>();
			fields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
			fields.add(new Field(new Location[]{new Location("companyCode")},"Company Code",4,String.class));
			fields.add(new Field(new Location[]{new Location("generatedFrom")},"Generated From",8,String.class));
			fields.add(new Field(new Location[]{new Location("debtorCode")},"Debtor Code",10,String.class));
			fields.add(new Field(new Location[]{new Location("abbreviatedName")},"Debtor Abbreviated Name",15,String.class));
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
			fields.add(new Field(new Location[]{new Location("statutoryInvoiceNumber")},"Statutory Invoice Number",150,String.class));
			fields.add(new Field(new Location[]{new Location("documentId")},"Document Id",16,String.class));
			fields.add(new Field(new Location[]{new Location("nobo")},"NOBO",4,String.class));
			fields.add(new Field(new Location[]{new Location("acronNewBranchCode")},"Acon New Branch Code",2,String.class));
			fields.add(new Field(new Location[]{new Location("acronNewCentrerCode")},"Acon New Centre Code",4,String.class));
			fields.add(new Field(new Location[]{new Location("itemParticular")},"Item Particular",50,String.class));
			fields.add(new Field(new Location[]{new Location("policyNumber")},"Policy Number",18,String.class));
			fields.add(new Field(new Location[]{new Location("endorsementNumber")},"Endorsement Number",3,String.class));
			fields.add(new Field(new Location[]{new Location("insurerClass")},"Insurer Class",3,String.class));
			fields.add(new Field(new Location[]{new Location("insurerCode")},"Insurer Code",10,String.class));
			fields.add(new Field(new Location[]{new Location("nacoraItemEffectiveDate")},"Effective Date of Nacora Item",10,XMLGregorianCalendar.class));
			fields.add(new Field(new Location[]{new Location("nacoraItemExpiryDate")},"Expiry Date of Nacora Item",10,XMLGregorianCalendar.class));
			fields.add(new Field(new Location[]{new Location("clientId")},"Client ID",12,String.class));
			fields.add(new Field(new Location[]{new Location("serviceDate")},"Service Date",5,String.class));
			fields.add(new Field(new Location[]{new Location("batchReference")},"Batch Reference",12,String.class));
			fields.add(new Field(new Location[]{new Location("a2kTransactionNumber")},"A2K Transaction",5,String.class));
			fields.add(new Field(new Location[]{new Location("a2rNumber")},"A2R Number",14,String.class));
			fields.add(new Field(new Location[]{new Location("businessKey")},"Business Key",50,String.class));
			fields.add(new Field(new Location[]{new Location("itemPrefixCode")},"Item Prefix Code",10,String.class));
			fields.add(new Field(new Location[]{new Location("itemParticular2")},"Item Particular 2",50,String.class));
			fields.add(new Field(new Location[]{new Location("itemParticular3")},"Item Particular 3",50,String.class));
			fields.add(new Field(new Location[]{new Location("itemParticular4")},"Item Particular 4",50,String.class));
			fields.add(new Field(new Location[]{new Location("withholdingTaxAmount")},"Withholding Tax Amount",22,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("withholdingVatAmount")},"Withholding VAT Amount",22,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("manualItemOnlyFlag")},"Manual Item Flag",1,String.class));
			fields.add(new Field(new Location[]{new Location("vatSubtotalFcAmount")},"VAT Subtotal FC Amount",22,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("vatSubtotalVatableLcAmount")},"VAT Subtotal Vatable LC Amount",22,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("vatSubtotalLcAmount")},"VAT Subtotal LC Amount",22,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("vatSubtotalPercentage")},"VAT Subtotal Percentage",5,BigDecimal.class));
			fields.add(new Field(new Location[]{new Location("subtotalVatCode")},"VAT Subtotal VAT Code",2,String.class));
			fields.add(new Field(new Location[]{new Location("originalDocumentNumber")},"Original Document Number",20,String.class));
			fields.add(new Field(new Location[]{new Location("placeOfSupply")},"Place Of Supply",70,String.class));
			file.setFields(fields);
			file.setRootType(InvoiceARHeader.class);
			file.setDelimiter("\\^");
			
			DelimitedFileDefinition dtlfile = new DelimitedFileDefinition();
			List<Field> dtlfields = new ArrayList<>();
			dtlfields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Record ID",3,String.class));
			dtlfields.add(new Field(new Location[]{new Location("sequenceNumber")},"Sequence No.",6,String.class));
			dtlfields.add(new Field(new Location[]{new Location("a2kJobFileNumber")},"A2K Job File No.",17,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeCode")},"Charge Code",4,String.class));
			dtlfields.add(new Field(new Location[]{new Location("trackingNumber")},"Tracking No.",10,String.class));
			dtlfields.add(new Field(new Location[]{new Location("profitCentre")},"Profit Centre",4,String.class));
			dtlfields.add(new Field(new Location[]{new Location("filePeriod")},"File Period",6,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeCategory")},"Charge Category",3,String.class));
			dtlfields.add(new Field(new Location[]{new Location("receiverProfitCentre")},"Receiver Profit Centre",4,String.class));
			dtlfields.add(new Field(new Location[]{new Location("glAccountNumber")},"GL Account Number",13,String.class));
			dtlfields.add(new Field(new Location[]{new Location("creditorCode")},"Creditor Code",10,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeLineLcAmount")},"Charge Line LC Amount",22,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeLineFcAmount")},"Charge Line FC Amount",22,String.class));
			for(int i = 0; i<14;i++) {
				dtlfields.add(new Field(new Location[]{new Location(FileObjectMappingUtility.DO_NOT_MAP)},"Province Code",2,String.class));
				dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("vatCode")},"VAT Code",2,String.class));
				dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("vatPercentage")},"VAT Percentage",5,String.class));
				dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("chargeLineVatLcAmount")},"Charge Line VAT LC Amount",22,String.class));
				dtlfields.add(new Field(new Location[]{new Location("lineVats",true,i),new Location("chargeLineVatFcAmount")},"Charge Line VAT FC Amount",22,String.class));
			}
			dtlfields.add(new Field(new Location[]{new Location("billingCompletedIndicator")},"Billing Completed Indicator",1,String.class));
			dtlfields.add(new Field(new Location[]{new Location("creditRequestNumber")},"Credit Request Number",17,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeLineRemarks")},"Charge Line Remarks",50,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeLineVatLcAmountLineLevel")},"Charge Line VAT LC Amount",22,String.class));
			dtlfields.add(new Field(new Location[]{new Location("opsChargeCode")},"Ops Charge Code",3,String.class));
			dtlfields.add(new Field(new Location[]{new Location("chargeLineParticular")},"Charge Line Particular",1000,String.class));
			dtlfields.add(new Field(new Location[]{new Location("customerReference")},"Customer Reference",150,String.class));
			dtlfields.add(new Field(new Location[]{new Location("partnerFileReference")},"Partner File Reference",17,String.class));
			dtlfields.add(new Field(new Location[]{new Location("hsnCode")},"HSN Code",30,String.class));
			dtlfile.setFields(dtlfields);
			dtlfile.setRootType(InvoiceARLine.class);
			dtlfile.setLocation(Arrays.asList(new Location("invoiceARLines",true)));
			dtlfile.setDelimiter("\\^");
			
			TMPL_MAP = new ComplexFileDefinition();
			TMPL_MAP.setRootType(InvoiceARHeader.class);
			TMPL_MAP.addRowType("HDR",file);
			TMPL_MAP.addRowType("DTL", dtlfile);
			TMPL_MAP.setTypeResetValue("HDR");
			TMPL_MAP.setTypeFieldSize(3);
	}
	
	@Override
	public DataType getType() {
		return DataType.AR;
	}

	@Override
	public void convertData(QueueItem item) throws Exception {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(item.getInputFile().getFile())){
			ComplexFileDefinition map;
			if(SourceSystem.TSBAS400.equals(item.getSourceSystem())) {
				map=AS400_MAP;
			}
			else if(SourceSystem.TM.equals(item.getSourceSystem())||SourceSystem.PL.equals(item.getSourceSystem())||SourceSystem.FLSI.equals(item.getSourceSystem())) {
				map=TMPL_MAP;
			}
			else {
				throw new UnsupportedSourceSystemException(item.getSourceSystem()+" is unsupported for "+item.getDataType());
			}
			List<Object> invoiceHeaders = fileMapper.getObjectFromComplexFile(bis, map);
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

	@Override
	public void transmitData(QueueItem item) throws Exception {
		genericTransmitData(item,"/pub/inbound/ar");		
	}

}
