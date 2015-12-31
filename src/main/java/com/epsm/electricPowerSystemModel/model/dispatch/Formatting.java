package com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;

public class Formatting {
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	protected DateTimeFormatter timeFormatter;
	
	public Formatting(){
		stringBuilder = new StringBuilder();
		numberFormatter = new DecimalFormat("0000.000", GlobalConstants.SYMBOLS);
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	}
}
