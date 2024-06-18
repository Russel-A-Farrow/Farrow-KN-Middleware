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
	private String itemParticular;
	private String clientId;
	private String itemParticular2;
	private String itemParticular3;
	private String itemParticular4;
	private BigDecimal vatSubtotalFcAmount;
	private BigDecimal vatSubtotalVatableLcAmount;
	private BigDecimal vatSubtotalLcAmount;
	private BigDecimal vatSubtotalPercentage;
	private String subtotalVatCode;
	private String originalDocumentNumber;
	
	private List<InvoiceARLine> invoiceARLines = new ArrayList<>();
}
