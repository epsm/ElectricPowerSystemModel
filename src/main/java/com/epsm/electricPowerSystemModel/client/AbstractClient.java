package com.epsm.electricPowerSystemModel.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration.Message;
import com.epsm.electricPowerSystemModel.util.UrlRequestSender;

@Import(UrlRequestSender.class)
public class AbstractClient<T extends Message> {

	@Autowired
	private UrlRequestSender<T> sender;
	
	protected void sendMessage(T message, String url){
		sender.sendObjectInJsonToUrlWithPOST(url, message);
	}
}
