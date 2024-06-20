package com.farrow.knmiddleware.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceARHeader {
	private String companyCode;
	private String generatedFrom;
	private String debtorCode;
	private String abbreviatedName;
	private String itemType;
	private String itemNumber;
	private XMLGregorianCalendar itemDate;
	private XMLGregorianCalendar itemDueDate;
	private String itemCurrencyCode;
	private String foreignCurrencyDecimialPlace;
	private String localCurrencyDecimialPlace;
	private BigDecimal itemExchangeRate;
	private BigDecimal lcAmount;
	private BigDecimal fcAmount;
	private BigDecimal vatLcAmount;
	private BigDecimal vatFcAmount;
	private String cashInvoiceInd;
	private String statutoryInvoiceNumber;
	private String documentId;
	private String nobo;
	private String acronNewBranchCode;
	private String acronNewCentrerCode;
	private String itemParticular;
	private String policyNumber;
	private String endorsementNumber;
	private String insurerClass;
	private String insurerCode;
	private XMLGregorianCalendar nacoraItemEffectiveDate;
	private XMLGregorianCalendar nacoraItemExpiryDate;
	private String clientId;
	private String serviceDate;
	private String branchReference;
	private String a2kTransactionNumber;
	private String a2rNumber;
	private String businessKey;
	private String itemPrefixCode;
	private String itemParticular2;
	private String itemParticular3;
	private String itemParticular4;
	private BigDecimal withholdingTaxAmount;
	private BigDecimal withholdingVatAmount;
	private String manualItemOnlyFlag;
	private BigDecimal vatSubtotalFcAmount;
	private BigDecimal vatSubtotalVatableLcAmount;
	private BigDecimal vatSubtotalLcAmount;
	private BigDecimal vatSubtotalPercentage;
	private String subtotalVatCode;
	private String originalDocumentNumber;
	private String placeOfSupply;
	
	private List<InvoiceARLine> invoiceARLines = new ArrayList<>();
}
