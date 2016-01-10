package com.epsm.electricPowerSystemModel.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.model.generation.PowerStationState;

@Component
public class PowerStationStateClient extends AbstractClient<PowerStationState>{

	@Value("${api.powerstation.acceptstate}")
	private String api;
	
	public void sendPowerStationState(PowerStationState state){
		sendMessage(state, api);
	}
}
