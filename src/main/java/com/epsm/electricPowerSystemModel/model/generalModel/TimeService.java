package com.epsm.electricPowerSystemModel.model.generalModel;

import java.time.LocalDateTime;

public class TimeService {
	public LocalDateTime getCurrentTime(){
		return LocalDateTime.now();
	}
}