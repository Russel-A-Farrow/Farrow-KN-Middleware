package com.farrow.knmiddleware.converters;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.InvoiceARHeader;
import com.farrow.knmiddleware.dto.InvoiceARLine;
import com.farrow.knmiddleware.dto.InvoiceARLineVat;
import com.kn.services.kninvoiceheader.InvoiceHeaderInfo;
import com.kn.services.kninvoiceheader.YesNoType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
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
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AccountingCostCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AccountingCostType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CalculationRateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PayableRoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PrepaidAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PricingCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.RoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

@Component("invoiceConverter")
public class InvoiceConverter extends KNObjectConverter<InvoiceType,InvoiceARHeader> {
	
	public InvoiceConverter() throws JAXBException {
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
		invoiceHeaderInfo.setStatutoryInvoiceNo(input.getStatutoryInvoiceNumber());
		invoiceHeaderInfo.setDocId(input.getDocumentId());
		invoiceHeaderInfo.setNobo(input.getNobo());
		invoiceHeaderInfo.setBranch(input.getAcronNewBranchCode());
		invoiceHeaderInfo.setCenter(input.getAcronNewCentrerCode());
		invoiceHeaderInfo.setPolicyNo(input.getPolicyNumber());
		invoiceHeaderInfo.setEndorsementNo(input.getEndorsementNumber());
		invoiceHeaderInfo.setInsurerClass(input.getInsurerClass());
		invoiceHeaderInfo.setInsurerCode(input.getInsurerCode());
		invoiceHeaderInfo.setNacoraItemEffectiveDate(input.getNacoraItemEffectiveDate());
		invoiceHeaderInfo.setNacoraItemExpiryDate(input.getNacoraItemExpiryDate());
		invoiceHeaderInfo.setClientID(input.getClientId());
		invoiceHeaderInfo.setServiceDate(input.getServiceDate());

		ExtensionContentType type = new ExtensionContentType();
		type.setAny(invoiceHeaderInfo);	
		UBLExtensionType ublType = new UBLExtensionType();
		ublType.setExtensionContent(type);
		UBLExtensionsType ublExtsType = new UBLExtensionsType();
		ublExtsType.getUBLExtension().add(ublType);
		invoice.setUBLExtensions(ublExtsType);	

		if(input.getGeneratedFrom()!=null) {
			SupplierPartyType supplierParty = new SupplierPartyType();
			PartyType supplierPartyParty = new PartyType();
			PartyNameType supplierPartyNameValue = new PartyNameType();
			NameType supplierNameTypeValue = new NameType();
			supplierNameTypeValue.setValue(input.getGeneratedFrom());
			supplierPartyNameValue.setName(supplierNameTypeValue);
			supplierPartyParty.getPartyName().add(supplierPartyNameValue);
			supplierParty.setParty(supplierPartyParty);
			invoice.setAccountingSupplierParty(supplierParty);
		}
		
		if(input.getItemType()!=null) {
			InvoiceTypeCodeType invoiceTypeCode = new InvoiceTypeCodeType();
			invoiceTypeCode.setValue(input.getItemType());
			invoice.setInvoiceTypeCode(invoiceTypeCode);
		}
		
		if(input.getItemNumber()!=null) {
			IDType itemType = new IDType();
			itemType.setValue(input.getItemNumber());
			invoice.setID(itemType);
		}
		
		if(input.getItemDate()!=null) {
			IssueDateType issueDate = new IssueDateType();
			issueDate.setValue(input.getItemDate());
			invoice.setIssueDate(issueDate);
		}
		
		if(input.getItemCurrencyCode()!=null) {
			PricingCurrencyCodeType pricingCurrencyCode = new PricingCurrencyCodeType();
			pricingCurrencyCode.setValue(input.getItemCurrencyCode());
			invoice.setPricingCurrencyCode(pricingCurrencyCode);
		}
		
		if(input.getItemExchangeRate()!=null) {
			ExchangeRateType exchangeRate = new ExchangeRateType();
			CalculationRateType calculationRate = new CalculationRateType();
			calculationRate.setValue(input.getItemExchangeRate());
			exchangeRate.setCalculationRate(calculationRate);
			invoice.setPricingExchangeRate(exchangeRate);
		}
		
		if(input.getLcAmount()!=null||input.getFcAmount()!=null ||input.getWithholdingTaxAmount()!=null ||input.getWithholdingVatAmount()!=null ) {
			MonetaryTotalType monetaryTotal = new MonetaryTotalType();
			if(input.getLcAmount()!=null) {
				PayableAmountType payableAmount = new PayableAmountType();
				payableAmount.setValue(input.getLcAmount());
				monetaryTotal.setPayableAmount(payableAmount);
			}
			if(input.getFcAmount()!=null) {
				PayableRoundingAmountType payableRoundingAmount = new PayableRoundingAmountType();
				payableRoundingAmount.setValue(input.getFcAmount());
				monetaryTotal.setPayableRoundingAmount(payableRoundingAmount);
			}
			if(input.getWithholdingTaxAmount()!=null) {
				LineExtensionAmountType leat = new LineExtensionAmountType();
				leat.setValue(input.getWithholdingTaxAmount());
				monetaryTotal.setLineExtensionAmount(leat);
			}
			if(input.getWithholdingVatAmount()!=null) {
				PrepaidAmountType pat = new PrepaidAmountType();
				pat.setValue(input.getWithholdingTaxAmount());
				monetaryTotal.setPrepaidAmount(pat);
			}
			invoice.setLegalMonetaryTotal(monetaryTotal);
		}
		
		if(input.getVatFcAmount()!=null || input.getVatLcAmount()!=null) {
			TaxTotalType taxTotal = new TaxTotalType();
			if(input.getVatLcAmount()!=null) {
				TaxAmountType taxAmount = new TaxAmountType();
				taxAmount.setValue(input.getVatLcAmount());
				taxTotal.setTaxAmount(taxAmount);
			}
			if(input.getVatLcAmount()!=null) {
				RoundingAmountType roundingAmount = new RoundingAmountType();
				roundingAmount.setValue(input.getVatFcAmount());
				taxTotal.setRoundingAmount(roundingAmount);
			}
			invoice.getTaxTotal().add(taxTotal);
		}
		if(input.getItemParticular()!=null) {
			NoteType note = new NoteType();
			note.setValue(input.getItemParticular());
			invoice.getNote().add(note);
		}

		
		if(input.getBatchReference()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getBatchReference(),"Batch Reference"));
		}
		
		if(input.getA2kTransactionNumber()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getA2kTransactionNumber(),"A2K Transaction No."));
		}
		
		if(input.getA2rNumber()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getA2rNumber(),"A2R Number"));
		}
		if(input.getBusinessKey()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getBusinessKey(),"Business Key"));
		}
		
		if(input.getItemPrefixCode()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemPrefixCode(),"Item Prefix Code"));
		}
		
		if(input.getItemParticular2()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular2(),"Item Particular 2"));
		}
		if(input.getItemParticular3()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular3(),"Item Particular 3"));
		}
		if(input.getItemParticular4()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getItemParticular4(),"Item Particular 4"));
		}
		
		if(input.getManualItemOnlyFlag()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getManualItemOnlyFlag(),"MANUALAR"));
		}
		if(input.getVatSubtotalFcAmount()!=null || input.getVatSubtotalVatableLcAmount()!=null || input.getVatSubtotalLcAmount()!=null ||input.getVatSubtotalPercentage()!=null || input.getSubtotalVatCode()!=null) {
			TaxTotalType subTotalTaxTotal = new TaxTotalType();
			TaxSubtotalType taxSubtotal = new TaxSubtotalType();
			
			if(input.getVatSubtotalFcAmount()!=null) {
				RoundingAmountType subTotalRoundingAmount = new RoundingAmountType();
				subTotalRoundingAmount.setValue(input.getVatSubtotalFcAmount());
				subTotalTaxTotal.setRoundingAmount(subTotalRoundingAmount);	
			}
			if(input.getVatSubtotalVatableLcAmount()!=null) {
				TaxableAmountType subTotalTaxableAmount = new TaxableAmountType();
				subTotalTaxableAmount.setValue(input.getVatSubtotalVatableLcAmount());
				taxSubtotal.setTaxableAmount(subTotalTaxableAmount);
			}
			if(input.getVatSubtotalLcAmount()!=null) {
				TaxAmountType subTotaltaxAmount = new TaxAmountType();
				subTotaltaxAmount.setValue(input.getVatSubtotalLcAmount());
				taxSubtotal.setTaxAmount(subTotaltaxAmount);
			}
			if(input.getVatSubtotalPercentage()!=null) {
				PercentType subtotalPercent = new PercentType();
				subtotalPercent.setValue(input.getVatSubtotalPercentage());
				taxSubtotal.setPercent(subtotalPercent);
			}
			if(input.getSubtotalVatCode()!=null) {
				TaxCategoryType taxCategory = new TaxCategoryType();
				IDType vatCode = new IDType();
				vatCode.setValue(input.getSubtotalVatCode());
				taxCategory.setID(vatCode);
				taxSubtotal.setTaxCategory(taxCategory);
			}
			
			subTotalTaxTotal.getTaxSubtotal().add(taxSubtotal);
			invoice.getTaxTotal().add(subTotalTaxTotal);
		}
		
		if(input.getOriginalDocumentNumber()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getOriginalDocumentNumber(),"Original Document Number"));
		}
		if(input.getPlaceOfSupply()!=null) {
			invoice.getAdditionalDocumentReference().add(getDocumentReference(input.getPlaceOfSupply(),"POS"));
		}
		
		for (InvoiceARLine line: input.getInvoiceARLines()) {
			InvoiceLineType invoiceLine = new InvoiceLineType();
			
			if(line.getSequenceNumber()!=null) {
				IDType lineId = new IDType();
				lineId.setValue(line.getSequenceNumber());
				invoiceLine.setID(lineId);
			}
			if(line.getChargeCode()!=null) {
				AccountingCostType accountCost = new AccountingCostType();
				accountCost.setValue(line.getChargeCode());
				invoiceLine.setAccountingCost(accountCost);
			}
			if(line.getTrackingNumber()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getTrackingNumber()));
			}
			if(line.getProfitCentre()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getProfitCentre()));
			}
			if(line.getFilePeriod()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getFilePeriod()));
			}
			if(line.getChargeCategory()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getChargeCategory()));
			}
			if(line.getReceiverProfitCentre()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getReceiverProfitCentre()));
			}
			if(line.getGlAccountNumber()!=null) {
				AccountingCostCodeType acct = new AccountingCostCodeType();
				acct.setValue(line.getGlAccountNumber());
				invoiceLine.setAccountingCostCode(acct);
			}
			if(line.getCreditorCode()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getCreditorCode()));
			}
			if(line.getChargeLineLcAmount()!=null) {
				PriceType price = new PriceType();
				PriceAmountType priceAmount = new PriceAmountType();
				priceAmount.setValue(line.getChargeLineLcAmount());
				price.setPriceAmount(priceAmount);
				invoiceLine.setPrice(price);
			}
			if(line.getChargeLineFcAmount()!=null) {
				LineExtensionAmountType lineExtensionAmount = new LineExtensionAmountType();
				lineExtensionAmount.setValue(line.getChargeLineFcAmount());
				invoiceLine.setLineExtensionAmount(lineExtensionAmount);
			}
			for (InvoiceARLineVat lineVat:line.getLineVats()) {
				if(lineVat.getVatCode()!=null) {
					TaxTotalType lineTaxTotal = new TaxTotalType();
					TaxSubtotalType lineTaxSubTotal = new TaxSubtotalType();
					TaxCategoryType lineTaxCategory = new TaxCategoryType();
					IDType lineTaxCatoryId = new IDType();
					lineTaxCatoryId.setValue(lineVat.getVatCode());
					lineTaxCategory.setID(lineTaxCatoryId);
					lineTaxSubTotal.setTaxCategory(lineTaxCategory);
					
					if(lineVat.getVatPercentage()!=null) {
						PercentType linePercent = new PercentType();
						linePercent.setValue(lineVat.getVatPercentage());
						lineTaxSubTotal.setPercent(linePercent);
						lineTaxTotal.getTaxSubtotal().add(lineTaxSubTotal);
					}
					
					if(lineVat.getChargeLineVatLcAmount()!=null) {
						TaxAmountType lineTaxAmount = new TaxAmountType();
						lineTaxAmount.setValue(lineVat.getChargeLineVatLcAmount());
						lineTaxTotal.setTaxAmount(lineTaxAmount);
					}
					if(lineVat.getChargeLineVatFcAmount()!=null) {
						RoundingAmountType lineRoundingAmount = new RoundingAmountType();
						lineRoundingAmount.setValue(lineVat.getChargeLineVatFcAmount());
						lineTaxTotal.setRoundingAmount(lineRoundingAmount);
					}
					invoiceLine.getTaxTotal().add(lineTaxTotal);
				}
			}
			
			if(line.getBillingCompletedIndicator()!=null) {
				BillingReferenceType lineBillingReference = new BillingReferenceType();
				lineBillingReference.setAdditionalDocumentReference(getDocumentReference(line.getBillingCompletedIndicator()));
				invoiceLine.getBillingReference().add(lineBillingReference);
			}
			if(line.getCreditRequestNumber()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getCreditRequestNumber()));
			}
			if(line.getChargeLineRemarks()!=null) {
				NoteType lineNote = new NoteType();
				lineNote.setValue(line.getChargeLineRemarks());
				invoiceLine.setNote(lineNote);
			}
			if(line.getChargeLineVatLcAmountLineLevel()!=null) {
				TaxTotalType lineTaxTotal = new TaxTotalType();
				TaxSubtotalType lineTaxSubTotal = new TaxSubtotalType();
				TaxAmountType lineTaxAmount = new TaxAmountType();
				lineTaxAmount.setValue(line.getChargeLineLcAmount());
				lineTaxSubTotal.setTaxAmount(lineTaxAmount);
				lineTaxTotal.getTaxSubtotal().add(lineTaxSubTotal);
				invoiceLine.getTaxTotal().add(lineTaxTotal);
			}
			if(line.getOpsChargeCode()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getOpsChargeCode()));
			}
			if(line.getChargeLineParticular()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getChargeLineParticular()));
			}
			if(line.getCustomerReference()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getCustomerReference()));
			}
			if(line.getPartnerFileReference()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getPartnerFileReference()));
			}
			if(line.getHsnCode()!=null) {
				invoiceLine.getDocumentReference().add(getDocumentReference(line.getHsnCode()));
			}
			invoice.getInvoiceLine().add(invoiceLine);
		}
		
		return invoice;
	}

	@Override
	public JAXBElement<InvoiceType> createObject(InvoiceType input) {
		return new ObjectFactory().createInvoice(input);
	}
	
	private DocumentReferenceType getDocumentReference(String value, String id) {
		IDType idType = new IDType();
		idType.setValue(id);
		DocumentReferenceType documentReference = new DocumentReferenceType();
		documentReference.setID(idType);
		DocumentTypeType docType = new DocumentTypeType();
		docType.setValue(value);
		documentReference.setDocumentType(docType);
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
