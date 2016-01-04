package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;

public abstract class MessageInclusion implements Comparable<MessageInclusion>{
	protected int inclusionNumber;
	protected StringBuilder stringBuilder;
	protected DecimalFormat numberFormatter;
	protected DateTimeFormatter timeFormatter;

	public MessageInclusion(int inclusionNumber) {
		this.inclusionNumber = inclusionNumber;
		stringBuilder = new StringBuilder();
		numberFormatter = new DecimalFormat("0000.000", GlobalConstants.SYMBOLS);
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	}
	
	public int getInclusionNumber() {
		return inclusionNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + inclusionNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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
	public int compareTo(MessageInclusion other) {
		if(inclusionNumber - other.inclusionNumber < 0){
			return -1;
		}else if(inclusionNumber - other.inclusionNumber > 0){
			return +1;
		}
		
		return 0;
	}
	
	public abstract String toString();
}
