package main.java.com.yvaleriy85.esm.model;

public class Generator{
	private ControlUnit controlUnit;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	private boolean isTurnedOn;
	
	public float getGenerationInMW(){
		if(isTurnedOn){
			return controlUnit.getGeneratorPowerInMW();
		}else{
			return 0;
		}
	}

	public ControlUnit getControlUnit() {
		return controlUnit;
	}
	
	public void setControlUnit(ControlUnit controlUnit) {
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

	public void turnOnGenerator(){
		isTurnedOn = true;
	}
	
	public void turnOffGenerator(){
		isTurnedOn = false;
	}
}
