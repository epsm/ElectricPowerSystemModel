package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class MessageInclusion extends Formatting implements Comparable<MessageInclusion>{
	protected int inclusionNumber;

	public MessageInclusion(int inclusionNumber) {
		this.inclusionNumber = inclusionNumber;
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
}
