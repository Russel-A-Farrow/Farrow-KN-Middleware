package com.farrow.knmiddleware.converters;

import com.farrow.knmiddleware.dto.KNObjectConverter;
import com.kn.services.kninvoiceheader.InvoiceHeaderInfo;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.InvoiceARHeader;

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

		ExtensionContentType type = new ExtensionContentType();

		type.setAny(invoiceHeaderInfo);
		

		//
		SupplierPartyType supplierParty = new SupplierPartyType();
		PartyType supplierPartyParty = new PartyType();
		PartyNameType supplierPartyNameValue = new PartyNameType();
		NameType supplierNameTypeValue = new NameType();
		supplierNameTypeValue.setValue(input.getGeneratedFrom());
		supplierPartyNameValue.setName(supplierNameTypeValue);
		supplierPartyParty.getPartyName().add(supplierPartyNameValue);
		supplierParty.setParty(supplierPartyParty);
		invoice.setAccountingSupplierParty(supplierParty);
		
		
		
		
		IDType itemType = new IDType();
		itemType.setValue(input.getItemNumber());
		invoice.setID(itemType);
		
		UBLExtensionType ublType = new UBLExtensionType();
		ublType.setExtensionContent(type);
		UBLExtensionsType ublExtsType = new UBLExtensionsType();
		ublExtsType.getUBLExtension().add(ublType);
		invoice.setUBLExtensions(ublExtsType);
		
		
		
		return invoice;
	}

	@Override
	public JAXBElement<InvoiceType> createObject(InvoiceType input) {
		return new ObjectFactory().createInvoice(input);
	}

}
