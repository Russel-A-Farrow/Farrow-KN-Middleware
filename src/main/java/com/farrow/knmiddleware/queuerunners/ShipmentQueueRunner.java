package com.farrow.knmiddleware.queuerunners;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.converters.ShipmentDetailsConverter;
import com.farrow.knmiddleware.domain.mapping.DelimitedFileDefinition;
import com.farrow.knmiddleware.domain.mapping.Field;
import com.farrow.knmiddleware.domain.mapping.Location;
import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.DataType;
import com.farrow.knmiddleware.dto.QueueFile;
import com.farrow.knmiddleware.dto.QueueItem;
import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.farrow.knmiddleware.dto.SourceSystem;
import com.farrow.knmiddleware.exceptions.UnsupportedSourceSystemException;
import com.kn.services.accuredexpenses.AccruedExpenses;
import com.kn.services.shipmentdetails.ShipmentDetails;

@Component
public class ShipmentQueueRunner extends QueueRunner  {

	
	
	@Autowired ShipmentDetailsConverter shipmetnConverter;
	
	@Override
	public DataType getType() {
		return DataType.SHIPMENT;
	}

	public static final DelimitedFileDefinition TMPL_MAP;
	
	static {
		TMPL_MAP = new DelimitedFileDefinition();
		List<Field> fields = new ArrayList<>();
		fields.add(new Field(new Location[]{new Location("companyCode")},"company Code",10,String.class));
		fields.add(new Field(new Location[]{new Location("trackingNo")},"tracking No",10,String.class));
		fields.add(new Field(new Location[]{new Location("profitCentre")},"profit Centre",10,String.class));
		fields.add(new Field(new Location[]{new Location("filePeriod")},"file Period",10,String.class));
		fields.add(new Field(new Location[]{new Location("fileType")},"fileType",10,String.class));
		fields.add(new Field(new Location[]{new Location("coverFileTrackingNo")},"coverFileTrackingNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("airSeaInd")},"airSeaInd",10,String.class));
		fields.add(new Field(new Location[]{new Location("exportImportInd")},"exportImportInd",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipmentType")},"shipmentType",10,String.class));
		fields.add(new Field(new Location[]{new Location("originCountry")},"originCountry",10,String.class));
		fields.add(new Field(new Location[]{new Location("originLocation")},"originLocation",10,String.class));
		fields.add(new Field(new Location[]{new Location("destinationCountry")},"destinationCountry",10,String.class));
		fields.add(new Field(new Location[]{new Location("destinationLocation")},"destinationLocation",10,String.class));
		fields.add(new Field(new Location[]{new Location("bfSegment")},"bfSegment",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipper")},"shipper",10,String.class));
		fields.add(new Field(new Location[]{new Location("consignee")},"consignee",10,String.class));
		fields.add(new Field(new Location[]{new Location("principal")},"principal",10,String.class));
		fields.add(new Field(new Location[]{new Location("incoterms")},"incoterms",10,String.class));
		fields.add(new Field(new Location[]{new Location("airfreightProduct")},"airfreightProduct",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipperReference")},"shipperReference",10,String.class));
		fields.add(new Field(new Location[]{new Location("consigneeReference")},"consigneeReference",10,String.class));
		fields.add(new Field(new Location[]{new Location("principalReference")},"principalReference",10,String.class));
		fields.add(new Field(new Location[]{new Location("etdEtsDate")},"etdEtsDate",10,LocalDate.class));
		fields.add(new Field(new Location[]{new Location("etaDate")},"etaDate",10,LocalDate.class));
		fields.add(new Field(new Location[]{new Location("consignmentID")},"consignmentID",10,String.class));
		fields.add(new Field(new Location[]{new Location("mawbNo")},"mawbNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("hawbNo")},"hawbNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("branchOfficeNo")},"branchOfficeNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("exitLocationPort")},"exitLocationPort",10,String.class));
		fields.add(new Field(new Location[]{new Location("entryLocationPort")},"entryLocationPort",10,String.class));
		fields.add(new Field(new Location[]{new Location("exportCielFileRef")},"exportCielFileRef",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipperName")},"shipperName",10,String.class));
		fields.add(new Field(new Location[]{new Location("consigneeName")},"consigneeName",10,String.class));
		fields.add(new Field(new Location[]{new Location("principalName")},"principalName",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipperCID")},"shipperCID",10,String.class));
		fields.add(new Field(new Location[]{new Location("consigneeCID")},"consigneeCID",10,String.class));
		fields.add(new Field(new Location[]{new Location("principalCID")},"principalCID",10,String.class));
		fields.add(new Field(new Location[]{new Location("portOfExitCountry")},"portOfExitCountry",10,String.class));
		fields.add(new Field(new Location[]{new Location("portOfEntryCountry")},"portOfEntryCountry",10,String.class));
		fields.add(new Field(new Location[]{new Location("a2KJobFileNo")},"a2KJobFileNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("finalAgent")},"finalAgent",10,String.class));
		fields.add(new Field(new Location[]{new Location("finalAgentName")},"finalAgentName",10,String.class));
		fields.add(new Field(new Location[]{new Location("carrierCode")},"carrierCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("categoryCode")},"categoryCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("noOfPieces")},"noOfPieces",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("grossWeight")},"grossWeight",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("chargeableWeight")},"chargeableWeight",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("volumeInCBM")},"volumeInCBM",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("deliveryAgent")},"deliveryAgent",10,String.class));
		fields.add(new Field(new Location[]{new Location("deliveryAgentAconDebtor")},"deliveryAgentAconDebtor",10,String.class));
		fields.add(new Field(new Location[]{new Location("transportReference")},"transportReference",10,String.class));
		fields.add(new Field(new Location[]{new Location("operationalDepartment")},"operationalDepartment",10,String.class));
		fields.add(new Field(new Location[]{new Location("inlandSharingPercent")},"inlandSharingPercent",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("inlandSharingFromJobFileNo")},"inlandSharingFromJobFileNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("principalAconDebtor")},"principalAconDebtor",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPercent")},"sharingPercent",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerCompanyCode")},"sharingPartnerCompanyCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerAconDebtor")},"sharingPartnerAconDebtor",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerShortCode")},"sharingPartnerShortCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("inlandSharingToShortCode")},"inlandSharingToShortCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("inlandSharingToFilePeriod")},"inlandSharingToFilePeriod",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipmentStatusType")},"shipmentStatusType",10,String.class));
		fields.add(new Field(new Location[]{new Location("shipmentStatusCode")},"shipmentStatusCode",10,String.class));
		fields.add(new Field(new Location[]{new Location("masterJobFileRef")},"masterJobFileRef",10,String.class));
		fields.add(new Field(new Location[]{new Location("transportDocType")},"transportDocType",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPercent2")},"sharingPercent2",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerCompanyCode2")},"sharingPartnerCompanyCode2",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerAconDebtor2")},"sharingPartnerAconDebtor2",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingPartnerShortCode2")},"sharingPartnerShortCode2",10,String.class));
		fields.add(new Field(new Location[]{new Location("gpMarginCommAgentSharingPartnerAconCreditor")},"gpMarginCommAgentSharingPartnerAconCreditor",10,String.class));
		fields.add(new Field(new Location[]{new Location("gpMarginCommAgentSharingPercent")},"gpMarginCommAgentSharingPercent",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("gpMarginCommSalesSharingPartnerAconCreditor")},"gpMarginCommSalesSharingPartnerAconCreditor",10,String.class));
		fields.add(new Field(new Location[]{new Location("gpMarginCommSalesSharingPercent")},"gpMarginCommSalesSharingPercent",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("previousFSLProfitCentre")},"previousFSLProfitCentre",10,String.class));
		fields.add(new Field(new Location[]{new Location("previousFSLFilePeriod")},"previousFSLFilePeriod",10,String.class));
		fields.add(new Field(new Location[]{new Location("previousFSLBranchOfficeNo")},"previousFSLBranchOfficeNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("previousFSLA2KJobFileNo")},"previousFSLA2KJobFileNo",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingAmount")},"sharingAmount",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("sharingCurrency")},"sharingCurrency",10,String.class));
		fields.add(new Field(new Location[]{new Location("sharingAmount2")},"sharingAmount2",10,BigDecimal.class));
		fields.add(new Field(new Location[]{new Location("sharingCurrency2")},"sharingCurrency2",10,String.class));

		TMPL_MAP.setFields(fields);
		TMPL_MAP.setRootType(AccrualDTO.class);
		TMPL_MAP.setDelimiter("\\^");
	}
	
	
	@Override
	public void convertData(QueueItem item) throws Exception {
		if(SourceSystem.TSB.equals(item.getSourceSystem())) {

			ShipmentDetailsDTO shipmentDetails = mapper.readValue(item.getInputFile().getFile(), ShipmentDetailsDTO.class);
			ShipmentDetails invoice = shipmetnConverter.convertToKNObject(shipmentDetails);
			byte[] xmlFile = shipmetnConverter.generateXml(invoice);
			QueueFile xmlQueueFile = new QueueFile();
			xmlQueueFile.setFile(xmlFile);
			item.setOutputXml(xmlQueueFile);
			queueDao.saveOutputFile(item.getId(),item.getOutputXml());
			
			
		}
		else if(SourceSystem.TM.equals(item.getSourceSystem())||SourceSystem.PL.equals(item.getSourceSystem())||SourceSystem.FLSI.equals(item.getSourceSystem())) {
			try(ByteArrayInputStream bis = new ByteArrayInputStream(item.getInputFile().getFile())){
				List<Object> shipmentLines = fileMapper.getObjectFromSimpleFile(bis, TMPL_MAP);
				
				byte[] objectFile = mapper.writeValueAsBytes(shipmentLines);
				QueueFile objectQueueFile = new QueueFile();
				objectQueueFile.setFile(objectFile);
				item.setObjectFile(objectQueueFile);
				queueDao.saveObjectFile(item.getId(),item.getObjectFile());
				
				ShipmentDetails shipmentDetails = shipmetnConverter.convertToKNObject(shipmentLines);
				byte[] xmlFile = shipmetnConverter.generateXml(shipmentDetails);
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
		genericTransmitData(item,"/pub/inbound/shipmentdetail");
		
	}

}
