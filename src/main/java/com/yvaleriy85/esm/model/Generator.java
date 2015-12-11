package main.java.com.yvaleriy85.esm.model;

public class Generator{
	private StaticControlUnit controlUnit;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	private boolean isTurnedOn;
	
	public float getGenerationInMW(){
		if(isTurnedOn){
			return controlUnit.getGeneratorPower();
		}else{
			return 0;
		}
	}

	public StaticControlUnit getControlUnit() {
		return controlUnit;
	}
	
	public void setControlUnit(StaticControlUnit controlUnit) {
		this.controlUnit = controlUnit;
	}

	public float getNominalPowerInMW() {
		return nominalPowerInMW;
	}

	public void setNominalPowerInMW(float nominalPowerInMW) {
		this.nominalPowerInMW = nominalPowerInMW;
	}

	public float getMinimalTechnologyPower() {
		return minimalTechnologyPower;
	}

	public void setMinimalTechnologyPower(float minimalTechnologyPower) {
		this.minimalTechnologyPower = minimalTechnologyPower;
	}

	public boolean isTurnedOn() {
		return isTurnedOn;
	}

	public void setTurnedOn(boolean isTurnedOn) {
		this.isTurnedOn = isTurnedOn;
	}
}
