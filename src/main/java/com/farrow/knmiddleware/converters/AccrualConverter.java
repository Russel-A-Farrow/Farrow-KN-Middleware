package com.farrow.knmiddleware.converters;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.AccrualDTO;
import com.farrow.knmiddleware.dto.KNObjectConverter;
import com.kn.services.accuredexpenses.AccruedExpenseType;
import com.kn.services.accuredexpenses.AccruedExpenses;
import com.kn.services.accuredexpenses.InterfaceStatusType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

@Component("AccrualConverter")
public class AccrualConverter extends KNObjectConverter<AccruedExpenses,AccrualDTO> {

	private static final QName _AccruedExpenses_QNAME = new QName("http://services.kn.com/xsd/acon/fsl/AccuredExpenses/v1", "AccruedExpenses");
	
	public AccrualConverter(JAXBContext context) throws JAXBException {
		super(JAXBContext.newInstance(AccruedExpenses.class,AccruedExpenseType.class));
	}

	@Override
	public AccruedExpenses convertToKNObject(AccrualDTO input) throws Exception {
		AccruedExpenses expenses = new AccruedExpenses();
		AccruedExpenseType expense = new AccruedExpenseType();
		expense.setCompanyCode(input.getCompanyCode());
		expense.setTxReference(input.getTxReference());
		expense.setCallingApplication(input.getCallingApplication());
		expense.setTxDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(input.getTransactionDate().toString()));
		expense.setCreditorCode(input.getCreditorCode());
		expense.setJobFileNo(input.getA2kJobFileNo());
		expense.setChargeCode(input.getChargeCode());
		expense.setTrackingNo(input.getTrackingNo());
		expense.setProfitCentre(input.getProfitCentre());
		expense.setFilePeriod(input.getFilePeriod());
		expense.setChargeCategory(input.getChargeCategory());
		expense.setCurrencyCode(input.getCurrencyCode());
		expense.setLCDecimalPlace(input.getLocalCurrencyDecimalPlace());
		expense.setAccrualLCAmount(input.getAccrualLCAmount());
		expense.setAWBNo(input.getAwbNo());
		expense.setDestLocCode(input.getDestinationLocationCode());
		expense.setWeight(input.getWeight());
		expense.setRemark(input.getRemark());
		expense.setSequenceNo(input.getSequenceNo());
		expense.setInterfaceStatus(InterfaceStatusType.valueOf(input.getInterfaceStatus()));
		expense.setCostType(input.getCostType());
		expense.setForeignCurrencyCode(input.getForeignCurrencyCode());
		expense.setAccrualFCAmount(input.getAccrualFcAcmount());
		expense.setLastUpdatedUser(input.getLastUpdatedUser());
		return expenses;
	}

	@Override
	public JAXBElement<AccruedExpenses> createObject(AccruedExpenses value) {
		return new JAXBElement<>(_AccruedExpenses_QNAME, AccruedExpenses.class, null, value);
	}

}
