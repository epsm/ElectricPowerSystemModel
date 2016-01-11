package com.epsm.electricPowerSystemModel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.epsm.electricPowerSystemModel.model.generalModel.DispatchingObjectsSource;
import com.epsm.electricPowerSystemModel.model.generalModel.TimeService;

@RestController
@RequestMapping("/")
public class IndicatorController {
	
	@Autowired
	private DispatchingObjectsSource source;
	
	@Autowired
	private TimeService timeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody String acceptConsumptionPermission(){
		return String.format("date and time on server: %s, objects in simulation: %s",
				timeService.getCurrentTime(), source.getDispatchingObjects());
	}
}