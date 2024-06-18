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
	private String chargeCode;
	private String trackingNumber;
	private String profitCentre;
	private String filePeriod;
	private String chargeCategory;
	private BigDecimal chargeLineLcAmount;
	private BigDecimal chargeLineFcAmount;
	private String vatCode;
	private BigDecimal vatPercentage;
	private BigDecimal chargeLineVatLcAmount;
	private BigDecimal chargeLineVatFcAmount;
	private String billingCompletedIndicator;
	private String chargeLineRemarks;
	private String chargeLineParticular;
	private String customerReference;
}
