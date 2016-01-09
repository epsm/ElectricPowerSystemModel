package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.Constants;

public abstract class MessageInclusion implements Comparable<MessageInclusion>{
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
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + inclusionNumber;
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(!(obj instanceof MessageInclusion)){
			return false;
		}
		
		MessageInclusion other = (MessageInclusion) obj;
		
		if(inclusionNumber != other.inclusionNumber){
			return false;
		}
		return true;
	}
	
	@Override
	public final int compareTo(MessageInclusion other) {
		if(inclusionNumber - other.inclusionNumber < 0){
			return -1;
		}else if(inclusionNumber - other.inclusionNumber > 0){
			return +1;
		}
		
		return 0;
	}
	
	public abstract String toString();
}
