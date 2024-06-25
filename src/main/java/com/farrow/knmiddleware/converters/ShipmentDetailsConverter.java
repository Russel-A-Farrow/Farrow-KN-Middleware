package com.farrow.knmiddleware.converters;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Component;

import com.farrow.knmiddleware.dto.KNObjectConverter;
import com.farrow.knmiddleware.dto.ShipmentDetailsDTO;
import com.kn.services.shipmentdetails.ShipmentDetailType;
import com.kn.services.shipmentdetails.ShipmentDetails;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;


@Component("shipmentDetailsConverter")
public class ShipmentDetailsConverter extends KNObjectConverter<ShipmentDetails,ShipmentDetailsDTO> {
	
	private static final QName _ShipmentDetails_QNAME = new QName("http://services.kn.com/xsd/acon/fsl/ShipmentDetails/v1", "ShipmentDetails");
	
	public ShipmentDetailsConverter() throws JAXBException {
		super(JAXBContext.newInstance(ShipmentDetails.class, ShipmentDetailType.class));
	}
	
	@Override
	public ShipmentDetails convertToKNObject(ShipmentDetailsDTO input) throws Exception {
		ShipmentDetails shipmentDetails = new ShipmentDetails();
		ShipmentDetailType shipmentDetail = new ShipmentDetailType();
		shipmentDetail.setCompanyCode(input.getCompanyCode());
		shipmentDetail.setTrackingNo(input.getTrackingNo());
		shipmentDetail.setProfitCentre(input.getProfitCentre());
		shipmentDetail.setFilePeriod(input.getFilePeriod());
		shipmentDetail.setFileType(input.getFileType());
		shipmentDetail.setCoverFileTrackingNo(input.getCoverFileTrackingNo());
		shipmentDetail.setAirSeaInd(input.getAirSeaInd());
		shipmentDetail.setExportImportInd(input.getExportImportInd());
		shipmentDetail.setShipmentType(input.getShipmentType());
		shipmentDetail.setOriginCountry(input.getOriginCountry());
		shipmentDetail.setOriginLocation(input.getOriginLocation());
		shipmentDetail.setDestinationCountry(input.getDestinationCountry());
		shipmentDetail.setDestinationLocation(input.getDestinationLocation());
		shipmentDetail.setBFSegment(input.getBfSegment());
		shipmentDetail.setShipper(input.getShipper());
		shipmentDetail.setConsignee(input.getConsignee());
		shipmentDetail.setPrincipal(input.getPrincipal());
		shipmentDetail.setIncoterms(input.getIncoterms());
		shipmentDetail.setAirfreightProduct(input.getAirfreightProduct());
		shipmentDetail.setShipperReference(input.getShipperReference());
		shipmentDetail.setConsigneeReference(input.getConsigneeReference());
		shipmentDetail.setPrincipalReference(input.getPrincipalReference());

		shipmentDetail.setEtdEtsDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(input.getEtdEtsDate().toString()));
		shipmentDetail.setEtaDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(input.getEtaDate().toString()));
		shipmentDetail.setConsignmentID(input.getConsignmentID());
		shipmentDetail.setMawbNo(input.getMawbNo());
		shipmentDetail.setHawbNo(input.getHawbNo());
		shipmentDetail.setBranchOfficeNo(input.getBranchOfficeNo());
		shipmentDetail.setExitLocationPort(input.getExitLocationPort());
		shipmentDetail.setEntryLocationPort(input.getEntryLocationPort());
		shipmentDetail.setExportCielFileRef(input.getExportCielFileRef());
		shipmentDetail.setShipperName(input.getShipperName());
		shipmentDetail.setConsigneeName(input.getConsigneeName());
		shipmentDetail.setPrincipalName(input.getPrincipalName());
		shipmentDetail.setShipperCid(input.getShipperCID());
		shipmentDetail.setConsigneeCid(input.getConsigneeCID());
		shipmentDetail.setPrincipalCid(input.getPrincipalCID());
		shipmentDetail.setPortOfExitCountry(input.getPortOfExitCountry());
		shipmentDetail.setPortOfEntryCountry(input.getPortOfEntryCountry());
		shipmentDetail.setA2KJobFileNo(input.getA2KJobFileNo());
		shipmentDetail.setFinalAgent(input.getFinalAgent());
		shipmentDetail.setFinalAgentName(input.getFinalAgentName());
		shipmentDetail.setCarrierCode(input.getCarrierCode());
		shipmentDetail.setCategoryCode(input.getCategoryCode());
		shipmentDetail.setNoOfPieces(input.getNoOfPieces());
		shipmentDetail.setGrossWeight(input.getGrossWeight());
		shipmentDetail.setChargeableWeight(input.getChargeableWeight());
		shipmentDetail.setVolumeInCBM(input.getVolumeInCBM());
		shipmentDetail.setDeliveryAgent(input.getDeliveryAgent());
		shipmentDetail.setDeliveryAgentAconDebtor(input.getDeliveryAgentAconDebtor());
		shipmentDetail.setTransportReference(input.getTransportReference());
		shipmentDetail.setOperationalDepartment(input.getOperationalDepartment());
		shipmentDetail.setInlandSharingPercent(input.getInlandSharingPercent());
		shipmentDetail.setInlandSharingFromJobFileNo(input.getInlandSharingFromJobFileNo());
		shipmentDetail.setPrincipalAconDebtor(input.getPrincipalAconDebtor());
		shipmentDetail.setSharingPercent(input.getSharingPercent());
		shipmentDetail.setSharingPartnerCompanyCode(input.getSharingPartnerCompanyCode());
		shipmentDetail.setSharingPartnerAconDebtor(input.getSharingPartnerAconDebtor());
		shipmentDetail.setSharingPartnerShortCode(input.getSharingPartnerShortCode());
		shipmentDetail.setInlandSharingToShortCode(input.getInlandSharingToShortCode());
		shipmentDetail.setInlandSharingToFilePeriod(input.getInlandSharingToFilePeriod());
		shipmentDetail.setShipmentStatusType(input.getShipmentStatusType());
		shipmentDetail.setShipmentStatusCode(input.getShipmentStatusCode());
		shipmentDetail.setMasterJobFileRef(input.getMasterJobFileRef());
		shipmentDetail.setTransportDocType(input.getTransportDocType());
		shipmentDetail.setSharingPercent2(input.getSharingPercent2());
		shipmentDetail.setSharingPartnerCompanyCode2(input.getSharingPartnerCompanyCode2());
		shipmentDetail.setSharingPartnerAconDebtor2(input.getSharingPartnerAconDebtor2());
		shipmentDetail.setSharingPartnerShortCode2(input.getSharingPartnerShortCode2());
		shipmentDetail.setGpMarginCommAgentSharingPartnerAconCreditor(input.getGpMarginCommAgentSharingPartnerAconCreditor());
		shipmentDetail.setGpMarginCommAgentSharingPercent(input.getGpMarginCommAgentSharingPercent());
		shipmentDetail.setGpMarginCommSalesSharingPartnerAconCreditor(input.getGpMarginCommSalesSharingPartnerAconCreditor());
		shipmentDetail.setGpMarginCommSalesSharingPercent(input.getGpMarginCommSalesSharingPercent());
		shipmentDetail.setPreviousFSLProfitCentre(input.getPreviousFSLProfitCentre());
		shipmentDetail.setPreviousFSLFilePeriod(input.getPreviousFSLFilePeriod());
		shipmentDetail.setPreviousFSLBranchOfficeNo(input.getPreviousFSLBranchOfficeNo());
		shipmentDetail.setPreviousFSLA2KJobFileNo(input.getPreviousFSLA2KJobFileNo());
		shipmentDetail.setSharingAmount(input.getSharingAmount());
		shipmentDetail.setSharingCurrency(input.getSharingCurrency());
		shipmentDetail.setSharingAmount2(input.getSharingAmount2());
		shipmentDetail.setSharingCurrency2(input.getSharingCurrency2());

		shipmentDetails.getShipmentDetail().add(shipmentDetail);
		return shipmentDetails;
	}

	@Override
	public JAXBElement<ShipmentDetails> createObject(ShipmentDetails value) {
		return new JAXBElement<>(_ShipmentDetails_QNAME, ShipmentDetails.class, null, value);
	}

}
