package com.farrow.knmiddleware.converters;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.dto.InvoiceARLine;
import com.farrow.knmiddleware.dto.KNObjectConverter;
import com.kn.services.kninvoiceheader.InvoiceHeaderInfo;
import com.kn.services.kninvoiceheader.YesNoType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExchangeRateType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AccountingCostType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CalculationRateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableRoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PricingCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.XPathType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

@Component("InvoiceConverter")
public class InvoiceConverter extends KNObjectConverter<InvoiceType,InvoiceARHeader> {
	
	public InvoiceConverter () throws JAXBException {
		super(JAXBContext.newInstance(InvoiceType.class,InvoiceHeaderInfo.class));
	}
	
	@Override
	public InvoiceType convertToKNObject(InvoiceARHeader input) {
		InvoiceType invoice  = new InvoiceType();

		InvoiceHeaderInfo invoiceHeaderInfo = new InvoiceHeaderInfo();
		invoiceHeaderInfo.setCompanyCode(input.getCompanyCode());
		invoiceHeaderInfo.setAccountCode(input.getDebtorCode());
		invoiceHeaderInfo.setItemDueDate(input.getItemDueDate());
		invoiceHeaderInfo.setFCDecimalPlace(input.getForeignCurrencyDecimialPlace());
		invoiceHeaderInfo.setLCDecimalPlace(input.getLocalCurrencyDecimialPlace());
		invoiceHeaderInfo.setCashInvoiceInd(YesNoType.fromValue(input.getCashInvoiceInd()));
		invoiceHeaderInfo.setClientID(input.getClientId());

		ExtensionContentType type = new ExtensionContentType();
		type.setAny(invoiceHeaderInfo);	
		UBLExtensionType ublType = new UBLExtensionType();
		ublType.setExtensionContent(type);
		UBLExtensionsType ublExtsType = new UBLExtensionsType();
		ublExtsType.getUBLExtension().add(ublType);
		invoice.setUBLExtensions(ublExtsType);	

		SupplierPartyType supplierParty = new SupplierPartyType();
		PartyType supplierPartyParty = new PartyType();
		PartyNameType supplierPartyNameValue = new PartyNameType();
		NameType supplierNameTypeValue = new NameType();
		supplierNameTypeValue.setValue(input.getGeneratedFrom());
		supplierPartyNameValue.setName(supplierNameTypeValue);
		supplierPartyParty.getPartyName().add(supplierPartyNameValue);
		supplierParty.setParty(supplierPartyParty);
		invoice.setAccountingSupplierParty(supplierParty);
		
		InvoiceTypeCodeType invoiceTypeCode = new InvoiceTypeCodeType();
		invoiceTypeCode.setValue(input.getItemType());
		invoice.setInvoiceTypeCode(invoiceTypeCode);
		
		IDType itemType = new IDType();
		itemType.setValue(input.getItemNumber());
		invoice.setID(itemType);
		
		IssueDateType issueDate = new IssueDateType();
		issueDate.setValue(input.getItemDate());
		invoice.setIssueDate(issueDate);
		
		PricingCurrencyCodeType pricingCurrencyCode = new PricingCurrencyCodeType();
		pricingCurrencyCode.setValue(input.getItemCurrencyCode());
		invoice.setPricingCurrencyCode(pricingCurrencyCode);
		
		ExchangeRateType exchangeRate = new ExchangeRateType();
		CalculationRateType calculationRate = new CalculationRateType();
		calculationRate.setValue(input.getItemExchangeRate());
		exchangeRate.setCalculationRate(calculationRate);
		invoice.setPricingExchangeRate(exchangeRate);
		
		MonetaryTotalType monetaryTotal = new MonetaryTotalType();
		PayableAmountType payableAmount = new PayableAmountType();
		payableAmount.setValue(input.getLcAmount());
		PayableRoundingAmountType payableRoundingAmount = new PayableRoundingAmountType();
		payableRoundingAmount.setValue(input.getFcAmount());
		monetaryTotal.setPayableAmount(payableAmount);
		monetaryTotal.setPayableRoundingAmount(payableRoundingAmount);
		invoice.setLegalMonetaryTotal(monetaryTotal);
		
		TaxTotalType taxTotal = new TaxTotalType();
		TaxAmountType taxAmount = new TaxAmountType();
		taxAmount.setValue(input.getVatLcAmount());
		RoundingAmountType roundingAmount = new RoundingAmountType();
		roundingAmount.setValue(input.getVatFcAmount());
		taxTotal.setTaxAmount(taxAmount);
		taxTotal.setRoundingAmount(roundingAmount);
		invoice.getTaxTotal().add(taxTotal);
		
		NoteType note = new NoteType();
		note.setValue(input.getItemParticular());
		invoice.getNote().add(note);
		
		invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular2(), 1));
		invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular3(), 2));
		invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular4(), 3));
		
		TaxTotalType subTotalTaxTotal = new TaxTotalType();
		TaxSubtotalType taxSubtotal = new TaxSubtotalType();
		RoundingAmountType subTotalRoundingAmount = new RoundingAmountType();
		subTotalRoundingAmount.setValue(input.getVatSubtotalFcAmount());
		TaxableAmountType subTotalTaxableAmount = new TaxableAmountType();
		subTotalTaxableAmount.setValue(input.getVatSubtotalVatableLcAmount());
		TaxAmountType subTotaltaxAmount = new TaxAmountType();
		subTotaltaxAmount.setValue(input.getVatSubtotalLcAmount());
		PercentType subtotalPercent = new PercentType();
		subtotalPercent.setValue(input.getVatSubtotalPercentage());
		TaxCategoryType taxCategory = new TaxCategoryType();
		IDType vatCode = new IDType();
		vatCode.setValue(input.getSubtotalVatCode());
		taxCategory.setID(vatCode);
		
		subTotalTaxTotal.setRoundingAmount(subTotalRoundingAmount);
		taxSubtotal.setTaxableAmount(subTotalTaxableAmount);
		taxSubtotal.setTaxAmount(subTotaltaxAmount);
		taxSubtotal.setPercent(subtotalPercent);
		taxSubtotal.setTaxCategory(taxCategory);
		subTotalTaxTotal.getTaxSubtotal().add(taxSubtotal);
		invoice.getTaxTotal().add(subTotalTaxTotal);
		
		invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getOriginalDocumentNumber(), 4));
		
		for (InvoiceARLine line: input.getInvoiceARLines()) {
			InvoiceLineType invoiceLine = new InvoiceLineType();
			
			IDType lineId = new IDType();
			lineId.setValue(line.getSequenceNumber());
			invoiceLine.setID(lineId);
			
			AccountingCostType accountCost = new AccountingCostType();
			accountCost.setValue(line.getChargeCode());
			invoiceLine.setAccountingCost(accountCost);
			
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getTrackingNumber()));
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getProfitCentre()));
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getFilePeriod()));
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getChargeCategory()));
			
			PriceType price = new PriceType();
			PriceAmountType priceAmount = new PriceAmountType();
			priceAmount.setValue(line.getChargeLineLcAmount());
			price.setPriceAmount(priceAmount);
			invoiceLine.setPrice(price);
			
			LineExtensionAmountType lineExtensionAmount = new LineExtensionAmountType();
			lineExtensionAmount.setValue(line.getChargeLineFcAmount());
			invoiceLine.setLineExtensionAmount(lineExtensionAmount);
			
			TaxTotalType lineTaxTotal = new TaxTotalType();
			TaxSubtotalType lineTaxSubTotal = new TaxSubtotalType();
			TaxCategoryType lineTaxCategory = new TaxCategoryType();
			IDType lineTaxCatoryId = new IDType();
			lineTaxCatoryId.setValue(line.getVatCode());
			lineTaxCategory.setID(lineTaxCatoryId);
			lineTaxSubTotal.setTaxCategory(taxCategory);
			
			PercentType linePercent = new PercentType();
			linePercent.setValue(line.getVatPercentage());
			taxSubtotal.setPercent(subtotalPercent);
			lineTaxTotal.getTaxSubtotal().add(lineTaxSubTotal);
			
			TaxableAmountType lineTaxAmount = new TaxableAmountType();
			lineTaxAmount.setValue(line.getChargeLineVatLcAmount());
			lineTaxTotal.setTaxAmount(subTotaltaxAmount);
			
			RoundingAmountType lineRoundingAmount = new RoundingAmountType();
			lineRoundingAmount.setValue(line.getChargeLineVatFcAmount());
			lineTaxTotal.setRoundingAmount(subTotalRoundingAmount);
			invoiceLine.getTaxTotal().add(lineTaxTotal);
			
			BillingReferenceType lineBillingReference = new BillingReferenceType();
			lineBillingReference.setAdditionalDocumentReference(getDocumentReference(line.getBillingCompletedIndicator()));
			invoiceLine.getBillingReference().add(lineBillingReference);
			
			NoteType lineNote = new NoteType();
			lineNote.setValue(line.getChargeLineRemarks());
			invoiceLine.setNote(lineNote);
			
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getChargeLineParticular()));
			invoiceLine.getDocumentReference().add(getDocumentReference(line.getCustomerReference()));			
		}
		
		return invoice;
	}

	@Override
	public JAXBElement<InvoiceType> createObject(InvoiceType input) {
		return new ObjectFactory().createInvoice(input);
	}
	
	private DocumentReferenceType getDocumentReference(String value, int id) {
		IDType idType = new IDType();
		idType.setValue(String.valueOf(id));
		DocumentReferenceType documentReference = new DocumentReferenceType();
		XPathType xPath = new XPathType();
		xPath.setValue(value);
		documentReference.getXPath().add(xPath);
		documentReference.setID(idType);
		return documentReference;
	}
	
	private DocumentReferenceType getDocumentReference(String value) {
		IDType idType = new IDType();
		idType.setValue(value);
		DocumentReferenceType documentReference = new DocumentReferenceType();
		documentReference.setID(idType);
		return documentReference;
	}

}
