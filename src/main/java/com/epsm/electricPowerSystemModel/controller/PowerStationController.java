package com.epsm.electricPowerSystemModel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epsm.electricPowerSystemModel.model.generation.PowerStationGenerationSchedule;
import com.epsm.electricPowerSystemModel.service.IncomingMessageService;

@RestController
@RequestMapping("/api/powerstation")
public class PowerStationController {
	private Logger logger = LoggerFactory.getLogger(PowerStationController.class);
	
	@Autowired
	private IncomingMessageService service;
	
	@RequestMapping(value="/command", method = RequestMethod.POST)
	public @ResponseBody void acceptPowerstationGenerationSchedule(
			@RequestBody PowerStationGenerationSchedule schedule){
		
		logger.debug("Received {}.", schedule);
		service.acceptCommand(schedule);
	}
}
