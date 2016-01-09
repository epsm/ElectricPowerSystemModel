package com.epsm.electricPowerSystemModel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.service.IncomingMessageService;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {
	
	@Autowired
	private IncomingMessageService service;
	
	@RequestMapping(value="/command", method = RequestMethod.POST)
	public @ResponseBody void acceptConsumptionPermission(
			@RequestBody ConsumptionPermissionStub permission){
		service.acceptCommand(permission);
	}
}
