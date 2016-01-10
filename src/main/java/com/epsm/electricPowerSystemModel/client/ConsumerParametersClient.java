package com.epsm.electricPowerSystemModel.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.model.consumption.ConsumerParametersStub;
import com.epsm.electricPowerSystemModel.util.UrlRequestSender;

@Import(UrlRequestSender.class)
@Component
public class ConsumerParametersClient extends AbstractClient<ConsumerParametersStub>{

	@Value("${api.consumer.esatblishconnection}")
	private String api;
	
	public void sendConsumerParameters(ConsumerParametersStub parameters){
		sendMessage(parameters, api);
	}
}
