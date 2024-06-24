package com.farrow.knmiddleware.soapclient;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.soap.SoapEnvelope;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.HttpComponents5ClientFactory;
import org.springframework.ws.transport.http.HttpComponents5MessageSender;

import com.farrow.knmiddleware.FarrowKnMiddlewareApplication;
import com.farrow.knmiddleware.utils.KNConstants;
import com.kn.services.xsd.acon.account.debtorinfoservice.v1_2.GetDebtorInfoRequest;
import com.kn.services.xsd.acon.account.debtorinfoservice.v1_2.GetDebtorInfoResponse;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateRequest;
import com.kn.services.xsd.acon.invoice.invoiceservice.v1.GetInvoiceDueDateResponse;

import kn._int.knesb.xsd.esb.audit.v01.Audit;
import kn._int.knesb.xsd.esb.audit.v01.Audit.BusinessKeys;
import kn._int.knesb.xsd.esb.routing.v1.Routing;
import kn._int.knesb.xsd.esb.routing.v1.Routing.Properties.Property;


public class KNSoapClient extends WebServiceGatewaySupport {
	
	private static final Logger log = LogManager.getLogger(KNSoapClient.class);
	
	@Value("${knwebservices.duedate.url}")
	private String dueDateUrl;
	
	@Value("${knwebservices.debtorinfo.url}")
	private String debtorInfoUrl;
	
	@Value("${deploymentLevel}")
	private String deploymentLevel;
	
	
	public GetInvoiceDueDateResponse getInvoiceDueDate(GetInvoiceDueDateRequest req) {
		GetInvoiceDueDateResponse resp = (GetInvoiceDueDateResponse) getWebServiceTemplate().marshalSendAndReceive(dueDateUrl,req, new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
				SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
				updateSoapHeader(soapHeader);
				
			}
			
		});
		return resp;
	}
	
	public GetDebtorInfoResponse getInvoiceDueDate(GetDebtorInfoRequest req) {
		GetDebtorInfoResponse resp = (GetDebtorInfoResponse) getWebServiceTemplate().marshalSendAndReceive(debtorInfoUrl,req, new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
				SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
				updateSoapHeader(soapHeader);
				
			}
			
		});
		return resp;
	}
	

	private void updateSoapHeader(SoapHeader soapHeader) throws XmlMappingException, IOException {
		Routing routing = new Routing();
		Routing.Properties props = new Routing.Properties();
		Property senderId = new Property();
		senderId.setName("KNESB_Routing_SenderId");
		senderId.setValue("FARROW_CANADA");
		Property senderInstance = new Property();
		senderInstance.setName("KNESB_Routing_SenderId");
		senderInstance.setValue(deploymentLevel);
		Property companyId = new Property();
		companyId.setName("KNESB_Routing_Company");
		companyId.setValue(KNConstants.FARROW_COMPANYCODE);
		props.getProperty().add(companyId);
		routing.setProperties(props);
		Audit audit = new Audit();
		audit.setApplicationID(KNConstants.CALLING_APPLICATION);
		BusinessKeys keys = new BusinessKeys();
		keys.setBusinessKey2(KNConstants.FARROW_COMPANYCODE);
		audit.setBusinessKeys(keys);
		audit.setRequestID(RandomStringUtils.randomAlphabetic(36));
		log.info("Request ID: {}",audit.getRequestID());
		audit.setCorrelationID(RandomStringUtils.randomAlphabetic(36));
		log.info("Correlation ID: {}",audit.getCorrelationID());
		getMarshaller().marshal(routing, soapHeader.getResult());
		getMarshaller().marshal(audit, soapHeader.getResult());
	}
}
