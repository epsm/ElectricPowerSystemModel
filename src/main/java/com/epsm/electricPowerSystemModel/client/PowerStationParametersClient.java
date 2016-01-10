package com.epsm.electricPowerSystemModel.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.model.generation.PowerStationParameters;

@Component
public class PowerStationParametersClient extends AbstractClient<PowerStationParameters>{

	@Value("${api.powerstation.esatblishconnection}")
	private String api;
	
	public void sendStationParameters(PowerStationParameters parameters){
		sendMessage(parameters, api);
	}
}
