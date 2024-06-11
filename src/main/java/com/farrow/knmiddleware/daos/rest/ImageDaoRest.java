package com.farrow.knmiddleware.daos.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import com.farrow.knmiddleware.daos.ImageDao;
import com.farrow.knmiddleware.dto.ImageFile;
import com.farrow.knmiddleware.utils.FileUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageDaoRest implements ImageDao {
	
	private final Logger logger = LogManager.getLogger(ImageDaoRest.class);
	
	@Value("${imageAPIURL}")
	private String imageAPIURL;
	
	@Autowired
	private ObjectMapper mapper;
	
	private static final String INVOICE_IMG_CLASS = "com.farrow.imagingapi.domain.imaging.InvoiceImage";

	@Override
	public ImageFile downloadInvoiceImage(int clientNo, int referenceNo, int invoiceNo) throws IOException {
		HttpPost httpPost = new HttpPost(imageAPIURL + "streamImage");
		httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		Map<String, Object> index = new HashMap<>();
		index.put("clientNo", clientNo);
		index.put("b3ReferenceNo", referenceNo);
        index.put("invoiceNo", invoiceNo);
        
		Map<String,Object> bodyMap = new HashMap<>();
		bodyMap.put("imageClass", INVOICE_IMG_CLASS);
		bodyMap.put("index", index);
		String req = mapper.writeValueAsString(bodyMap);
		StringEntity body = new StringEntity(req);

		httpPost.setEntity(body);
		try(CloseableHttpClient httpClient = HttpClients.createDefault();) {
			return httpClient.execute(httpPost, new HttpClientResponseHandler<ImageFile>() {
				@Override
				public ImageFile handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
					// Get HttpResponse Status
					ImageFile imageFile = null;
					if(response.getCode()>=200 && response.getCode() <300) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							// return it as a String
							String result = EntityUtils.toString(entity);
							imageFile = mapper.readValue(result, new TypeReference<ImageFile>(){});
							imageFile.setFileName(FileUtility.sanitizeFileName(imageFile.getFileName()));
						}
					}
					else {
						logger.error("Failed to dowload Image {}", response.getReasonPhrase());
						throw new HttpException("Failed to dowload Image for");
					}
					return imageFile;
				}

			});
		} catch (IOException e) {
			logger.error(e);
			throw e;
		} 
	}

}
