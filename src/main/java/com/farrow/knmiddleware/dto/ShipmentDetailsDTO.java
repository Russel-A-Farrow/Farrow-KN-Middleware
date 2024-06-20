package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShipmentDetailsDTO {

	private String companyCode;
	private String trackingNo;
	private String profitCentre;
	private String filePeriod;
	private String fileType;
	private String coverFileTrackingNo;
	private String airSeaInd;
	private String exportImportInd;
	private String shipmentType;
	private String originCountry;
	private String originLocation;
	private String destinationCountry;
	private String destinationLocation;
	private String bfSegment;
	private String shipper;
	private String consignee;
	private String principal;
	private String incoterms;
	private String airfreightProduct;
	private String shipperReference;
	private String consigneeReference;
	private String principalReference;
	private XMLGregorianCalendar etdEtsDate;
	private XMLGregorianCalendar etaDate;
	private String consignmentID;
	private String mawbNo;
	private String hawbNo;
	private String branchOfficeNo;
	private String exitLocationPort;
	private String entryLocationPort;
	private String exportCielFileRef;
	private String shipperName;
	private String consigneeName;
	private String principalName;
	private String shipperCID;
	private String consigneeCID;
	private String principalCID;
	private String portOfExitCountry;
	private String portOfEntryCountry;
	private String a2KJobFileNo;
	private String finalAgent;
	private String finalAgentName;
	private String carrierCode;
	private String categoryCode;
	private BigDecimal noOfPieces;
	private BigDecimal grossWeight;
	private BigDecimal chargeableWeight;
	private BigDecimal volumeInCBM;
	private String deliveryAgent;
	private String deliveryAgentAconDebtor;
	private String transportReference;
	private String operationalDepartment;
	private BigDecimal inlandSharingPercent;
	private String inlandSharingFromJobFileNo;
	private String principalAconDebtor;
	private BigDecimal sharingPercent;
	private String sharingPartnerCompanyCode;
	private String sharingPartnerAconDebtor;
	private String sharingPartnerShortCode;
	private String inlandSharingToShortCode;
	private String inlandSharingToFilePeriod;
	private String shipmentStatusType;
	private String shipmentStatusCode;
	private String masterJobFileRef;
	private String transportDocType;
	private BigDecimal sharingPercent2;
	private String sharingPartnerCompanyCode2;
	private String sharingPartnerAconDebtor2;
	private String sharingPartnerShortCode2;
	private String gpMarginCommAgentSharingPartnerAconCreditor;
	private BigDecimal gpMarginCommAgentSharingPercent;
	private String gpMarginCommSalesSharingPartnerAconCreditor;
	private BigDecimal gpMarginCommSalesSharingPercent;
	private String previousFSLProfitCentre;
	private String previousFSLFilePeriod;
	private String previousFSLBranchOfficeNo;
	private String previousFSLA2KJobFileNo;
	private BigDecimal sharingAmount;
	private String sharingCurrency;
	private BigDecimal sharingAmount2;
	private String sharingCurrency2;

}
