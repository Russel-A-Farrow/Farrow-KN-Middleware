package com.farrow.knmiddleware.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.farrow.knmiddleware.soapclient.KNSoapClient;
import com.kn.services.xsd.acon.account.debtorinfoservice.v1_2.GetDebtorInfoRequest;
import com.kn.services.xsd.acon.account.debtorinfoservice.v1_2.GetDebtorInfoResponse;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateRequest;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateResponse;

@RestController
@RequestMapping("/ws")
public class KNWebServicesController {
	
	@Autowired private KNSoapClient soapClient;

	@PostMapping(value="/duedate", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GetInvoiceDueDateResponse getDueDate(@RequestBody GetInvoiceDueDateRequest req) {
		
		return soapClient.getInvoiceDueDate(req);
	}
	
	@PostMapping(value="/debtorinfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GetDebtorInfoResponse getDebtorTerms(@RequestBody GetDebtorInfoRequest req) {
		
		return soapClient.getInvoiceDueDate(req);
	}
}