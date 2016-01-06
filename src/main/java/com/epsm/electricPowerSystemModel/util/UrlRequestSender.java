package com.epsm.electricPowerSystemModel.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UrlRequestSender {
	
	public void sendObjectInJsonToUrlWithPOST(String url, Object object) throws Exception{;
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
	}
	
	private void serialize(OutputStream outStream, Object object) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		
        mapper.writeValue(outStream, object);
	}
}
