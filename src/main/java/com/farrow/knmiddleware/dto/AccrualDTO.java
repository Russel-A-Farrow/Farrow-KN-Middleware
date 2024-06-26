package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonFormat(with = {JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES})
public class AccrualDTO {
	private String companyCode;
	private String txReference;
	private String callingApplication;
	private LocalDate transactionDate;
	private String creditorCode;
	private String a2kJobFileNo;
	private String chargeCode;
	private String trackingNo;
	private String profitCentre;
	private String filePeriod;
	private String chargeCategory;
	private String currencyCode;
	private String localCurrencyDecimalPlace;
	private BigDecimal accrualLCAmount;
	private String awbNo;
	private String destinationLocationCode;
	private BigDecimal weight;
	private String remark;
	private String sequenceNo;
	private String interfaceStatus;
	private String costType;
	private String foreignCurrencyCode;
	private BigDecimal accrualFcAcmount;
	private String lastUpdatedUser;
	
}
