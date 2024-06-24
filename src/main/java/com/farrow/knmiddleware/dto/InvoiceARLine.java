package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceARLine {
	private String sequenceNumber;
	private String a2kJobFileNumber;
	private String chargeCode;
	private String trackingNumber;
	private String profitCentre;
	private String filePeriod;
	private String chargeCategory;
	private String receiverProfitCentre;
	private String glAccountNumber;
	private String creditorCode;
	private BigDecimal chargeLineLcAmount;
	private BigDecimal chargeLineFcAmount;
	private String vatCode;
	private BigDecimal vatPercentage;
	private BigDecimal chargeLineVatLcAmount;
	private BigDecimal chargeLineVatFcAmount;
	private String vatCode2;
	private BigDecimal vatPercentage2;
	private BigDecimal chargeLineVatLcAmount2;
	private BigDecimal chargeLineVatFcAmount2;
	private String billingCompletedIndicator;
	private String creditRequestNumber;
	private String chargeLineRemarks;
	private String opsChargeCode;
	private String chargeLineParticular;
	private String customerReference;
	private String partnerFileReference;
	private String hsnCode;
}
