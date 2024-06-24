package com.farrow.knmiddleware;

import java.time.LocalDate;
import java.time.Month;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farrow.knmiddleware.soapclient.KNSoapClient;
import com.farrow.knmiddleware.utils.KNConstants;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.BusinessTypeType;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateRequest;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateResponse;
import com.kn.services.xsd.acon.common.v1.ItemTypeType;
import com.kn.services.xsd.acon.common.v1.YesNoType;

@SpringBootTest(properties= {"deploymentLevel=development"})
public class SoapTest {
	
	@Autowired private KNSoapClient client;
	
	@Test
	public void getInvoiceDueDate() throws DatatypeConfigurationException {
		GetInvoiceDueDateRequest req = new GetInvoiceDueDateRequest();
		/*
		 * <CompanyCode>US55</CompanyCode>
        <DebtorCode>034273</DebtorCode>
        <ItemType>I</ItemType>
        <ItemDate>2022-10-19</ItemDate>
        <TrackingNo>R002590612</TrackingNo>
        <ProfitCentre>0044</ProfitCentre>
        <BusinessType>O</BusinessType>
        <IsCashInvoice>N</IsCashInvoice>
        <CallingApplication>TSEND</CallingApplication>
        <ItemCurrency>USD</ItemCurrency>
		 */
		req.setCompanyCode("US55");
		req.setDebtorCode("034273");
		req.setItemType(ItemTypeType.I);
		req.setItemDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2022, 10, 19, 0, 0, 0, 0, 0));
		req.setTrackingNo("R002590612");
		req.setProfitCentre("0044");
		req.setBusinessType(BusinessTypeType.O);
		req.setIsCashInvoice(YesNoType.N);
		req.setCallingApplication(KNConstants.CALLING_APPLICATION);
		req.setItemCurrency("USD");
		GetInvoiceDueDateResponse resp = client.getInvoiceDueDate(req);
		
		System.out.println("Credit term: "+resp.getCreditTerm());
		System.out.println("Due Date: "+resp.getDueDate());
	}
}
