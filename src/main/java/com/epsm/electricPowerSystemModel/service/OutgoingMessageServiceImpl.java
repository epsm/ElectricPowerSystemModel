package com.epsm.electricPowerSystemModel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epsm.electricPowerSystemModel.client.ConsumerParametersClient;
import com.epsm.electricPowerSystemModel.client.ConsumerStateClient;
import com.epsm.electricPowerSystemModel.client.PowerStationParametersClient;
import com.epsm.electricPowerSystemModel.client.PowerStationStateClient;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;
import com.epsm.electricPowerSystemModel.model.dispatch.State;

@Component
public class OutgoingMessageServiceImpl implements OutgoingMessageSrvice {

	@Autowired
	private PowerStationParametersClient stationParametersClient;
	
	@Autowired
	private PowerStationStateClient stationStateCliesnt;
	
	@Autowired
	private ConsumerParametersClient consumerParametersClient;
	
	@Autowired
	private ConsumerStateClient consumerStateClient;
	
	@Override
	public void establishConnection(Parameters parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acceptState(State state) {
		// TODO Auto-generated method stub

	}
}
