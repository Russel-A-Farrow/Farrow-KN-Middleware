package com.farrow.knmiddleware.queuerunners;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.converters.AccrualConverter;
import com.farrow.knmiddleware.domain.mapping.DelimitedFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.farrow.knmiddleware.exceptions.UnsupportedSourceSystemException;
import com.kn.services.accuredexpenses.AccruedExpenses;


@Component
public class AccrualQueueRunner extends QueueRunner {

	@Autowired AccrualConverter accrualConverter;
	
	public static final DelimitedFileDefinition TMPL_MAP;
	
	static {
		TMPL_MAP = new DelimitedFileDefinition();
		List<Field> fields = new ArrayList<>();
		fields.add(new Field(new Location[]{new Location("companyCode")},"Company Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("txReference")},"Transaction Reference",15,String.class));
		fields.add(new Field(new Location[]{new Location("callingApplication")},"Calling Application",8,String.class));
		fields.add(new Field(new Location[]{new Location("transactionDate")},"Transaction Date",10,LocalDate.class));
		fields.add(new Field(new Location[]{new Location("creditorCode")},"Creditor Code",10,String.class));
		fields.add(new Field(new Location[]{new Location("a2kJobFileNo")},"A2K Job File Number",17,String.class));
		fields.add(new Field(new Location[]{new Location("chargeCode")},"charge Code",4,String.class));
		fields.add(new Field(new Location[]{new Location("trackingNo")},"tracking No",10,String.class));
		fields.add(new Field(new Location[]{new Location("profitCentre")},"profit Centre",4,String.class));
		fields.add(new Field(new Location[]{new Location("filePeriod")},"file Period",6,String.class));
		fields.add(new Field(new Location[]{new Location("chargeCategory")},"charge Category",3,String.class));
		fields.add(new Field(new Location[]{new Location("currencyCode")},"currency Code",3,String.class));
		fields.add(new Field(new Location[]{new Location("localCurrencyDecimalPlace")},"local Currency Decimal Place",1,String.class));
		fields.add(new Field(new Location[]{new Location("accrualLCAmount")},"accrual LC Amount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("awbNo")},"awbNo",12,String.class));
		fields.add(new Field(new Location[]{new Location("destinationLocationCode")},"destination Location Code",3,String.class));
		fields.add(new Field(new Location[]{new Location("weight")},"weight",5,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("remark")},"remark",50,String.class));
		fields.add(new Field(new Location[]{new Location("sequenceNo")},"sequenceNo",13,String.class));
		fields.add(new Field(new Location[]{new Location("interfaceStatus")},"interface Status",1,String.class));
		fields.add(new Field(new Location[]{new Location("costType")},"cost Type",1,String.class));
		fields.add(new Field(new Location[]{new Location("foreignCurrencyCode")},"foreign Currency Code",3,String.class));
		fields.add(new Field(new Location[]{new Location("accrualFcAcmount")},"accrual FC Acmount",22,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("lastUpdatedUser")},"last Updated User",50,String.class));
		TMPL_MAP.setFields(fields);
		TMPL_MAP.setRootType(AccrualDTO.class);
		TMPL_MAP.setDelimiter("\\^");
	}
	
	@Override
	public DataType getType() {
		return DataType.ACCRUAL;
	}

	@Override
	public void convertData(QueueItem item) throws Exception {
		if(SourceSystem.TSB.equals(item.getSourceSystem())) {

			AccrualDTO accrualDetails = mapper.readValue(item.getInputFile().getFile(), AccrualDTO.class);
			AccruedExpenses expenses = accrualConverter.convertToKNObject(accrualDetails);
			byte[] xmlFile = accrualConverter.generateXml(expenses);
			QueueFile xmlQueueFile = new QueueFile();
			xmlQueueFile.setFile(xmlFile);
			item.setOutputXml(xmlQueueFile);
			queueDao.saveOutputFile(item.getId(),item.getOutputXml());
			
			
		}
		else if(SourceSystem.TM.equals(item.getSourceSystem())||SourceSystem.PL.equals(item.getSourceSystem())||SourceSystem.FLSI.equals(item.getSourceSystem())) {
			try(ByteArrayInputStream bis = new ByteArrayInputStream(item.getInputFile().getFile())){
				List<Object> accrualLines = fileMapper.getObjectFromSimpleFile(bis, TMPL_MAP);
				
				byte[] objectFile = mapper.writeValueAsBytes(accrualLines);
				QueueFile objectQueueFile = new QueueFile();
				objectQueueFile.setFile(objectFile);
				item.setObjectFile(objectQueueFile);
				queueDao.saveObjectFile(item.getId(),item.getObjectFile());
				
				AccruedExpenses expenses = accrualConverter.convertToKNObject(accrualLines);
				byte[] xmlFile = accrualConverter.generateXml(expenses);
				QueueFile xmlQueueFile = new QueueFile();
				xmlQueueFile.setFile(xmlFile);
				item.setOutputXml(xmlQueueFile);
				queueDao.saveOutputFile(item.getId(),item.getOutputXml());
			}
		}
		else {
			throw new UnsupportedSourceSystemException(item.getSourceSystem()+" is unsupported for "+item.getDataType());
		}

	}

	@Override
	public void transmitData(QueueItem item) throws Exception {
		genericTransmitData(item,"/pub/inbound/accrual");

	}

}
