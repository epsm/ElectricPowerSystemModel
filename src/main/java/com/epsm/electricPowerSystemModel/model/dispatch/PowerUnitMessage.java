package com.epsm.electricPowerSystemModel.model.dispatch;

public abstract class PowerUnitMessage implements Comparable<PowerUnitMessage>{
	private int powerUnitNumber;

	public PowerUnitMessage(int powerUnitNumber) {
		this.powerUnitNumber = powerUnitNumber;
	}
	
	public int getPowerUnitNumber() {
		return powerUnitNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + powerUnitNumber;
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
		if(getClass() != obj.getClass()){
			return false;
		}
		
		PowerUnitMessage other = (PowerUnitMessage) obj;
		
		if(powerUnitNumber != other.powerUnitNumber){
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(PowerUnitMessage other) {
		if(powerUnitNumber - other.powerUnitNumber < 0){
			return -1;
		}else if(powerUnitNumber - other.powerUnitNumber > 0){
			return +1;
		}
		
		return 0;
	}
}
