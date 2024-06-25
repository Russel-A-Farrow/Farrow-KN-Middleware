package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;
import java.util.List;

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
	private List<InvoiceARLineVat> lineVats;
	private String billingCompletedIndicator;
	private String creditRequestNumber;
	private String chargeLineRemarks;
	private BigDecimal chargeLineVatLcAmountLineLevel;
	private String opsChargeCode;
	private String chargeLineParticular;
	private String customerReference;
	private String partnerFileReference;
	private String hsnCode;
}
