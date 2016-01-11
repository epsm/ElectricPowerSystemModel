package com.epsm.electricPowerSystemModel.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UrlRequestSender<T> {
	private Logger logger = LoggerFactory.getLogger(UrlRequestSender.class);
	
	public void sendObjectInJsonToUrlWithPOST(String url, T object){;
		try{
			logger.debug("Sending {} to url {}.", object, url);
			
			URL urlObject = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
			OutputStream outStream = null;
		
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.getResponseCode();
			connection.setDoOutput(true);
			outStream = connection.getOutputStream();
			serialize(outStream, object);
			outStream.flush();
			outStream.close();
			connection.getResponseCode();
		}catch(Exception e){
			logger.warn("Error sending POST request to url {}. ",url, e);
		}
	}
	
	private void serialize(OutputStream outStream, Object object){
		ObjectMapper mapper = new ObjectMapper();
		
        try {
			mapper.writeValue(outStream, object);
		} catch (IOException e) {
			logger.warn("Error in JSON serialization. ", e);
		}
	}
}
