package com.farrow.knmiddleware.converters;

import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.AccrualDTO;
import com.kn.services.accuredexpenses.AccruedExpenseType;
import com.kn.services.accuredexpenses.AccruedExpenses;
import com.kn.services.accuredexpenses.InterfaceStatusType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;

@Component("AccrualConverter")
public class AccrualConverter extends KNObjectConverter<AccruedExpenses,AccrualDTO> implements KNObjectSubTypeConverter<AccruedExpenses,AccrualDTO,AccruedExpenseType> {

	private static final QName _AccruedExpenses_QNAME = new QName("http://services.kn.com/xsd/acon/fsl/AccuredExpenses/v1", "AccruedExpenses");
	
	public AccrualConverter() throws JAXBException {
		super(JAXBContext.newInstance(AccruedExpenses.class,AccruedExpenseType.class));
	}

	public AccruedExpenseType convertToKNSubObject(AccrualDTO input) throws Exception{
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
		if(input.getInterfaceStatus()!=null) {
			expense.setInterfaceStatus(InterfaceStatusType.valueOf(input.getInterfaceStatus()));
		}
		expense.setCostType(input.getCostType());
		expense.setForeignCurrencyCode(input.getForeignCurrencyCode());
		expense.setAccrualFCAmount(input.getAccrualFcAcmount());
		expense.setLastUpdatedUser(input.getLastUpdatedUser());
		return expense;
	}
	
	public AccruedExpenses convertToKNObject(List<Object> inputs) throws Exception {
		AccruedExpenses expenses = new AccruedExpenses();
		for(Object input:inputs) {
			if(input instanceof AccrualDTO) {
				AccruedExpenseType expense = convertToKNSubObject((AccrualDTO)input);
				expenses.getAccruedExpense().add(expense);
			}
			else {
				throw new UnsupportedOperationException("Unknown class "+ input.getClass());
			}
		}
		return expenses;
	}
	
	@Override
	public AccruedExpenses convertToKNObject(AccrualDTO input) throws Exception {
		AccruedExpenses expenses = new AccruedExpenses();
		AccruedExpenseType expense = convertToKNSubObject(input);
		expenses.getAccruedExpense().add(expense);
		return expenses;
	}


	
	@Override
	public JAXBElement<AccruedExpenses> createObject(AccruedExpenses value) {
		return new JAXBElement<>(_AccruedExpenses_QNAME, AccruedExpenses.class, null, value);
	}

}
