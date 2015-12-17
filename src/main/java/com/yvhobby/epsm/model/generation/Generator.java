package main.java.com.yvhobby.epsm.model.generation;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public class Generator{
	private ControlUnit controlUnit;
	private AstaticRegulatioUnit regulationUnit;
	private float nominalPowerInMW;
	private float minimalTechnologyPower;
	private boolean isTurnedOn;
	private int id;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAstaticRegulationUnit(AstaticRegulatioUnit regulationUnit) {
		this.regulationUnit = regulationUnit;
	}
}
