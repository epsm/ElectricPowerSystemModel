package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public abstract class MessageInclusion{
	private int inclusionNumber;
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	protected DateTimeFormatter timeFormatter;

	public MessageInclusion(int inclusionNumber) {
		this.inclusionNumber = inclusionNumber;
		stringBuilder = new StringBuilder();
		numberFormatter = new DecimalFormat("0000.000", Constants.SYMBOLS);
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	}
	
	protected int getInclusionNumber() {
		return inclusionNumber;
	}
	
	public abstract String toString();
}
