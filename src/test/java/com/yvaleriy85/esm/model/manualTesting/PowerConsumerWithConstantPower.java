package test.java.com.yvaleriy85.esm.model.manualTesting;

import main.java.com.yvaleriy85.esm.model.consumption.PowerConsumer;
import main.java.com.yvaleriy85.esm.model.generalModel.ElectricPowerSystemSimulationImpl;

public class PowerConsumerWithConstantPower extends PowerConsumer{
	private ElectricPowerSystemSimulationImpl powerSystemSimulation;
	private final float STANDART_FREQUENCY = 50;
	private final int CONSTANT_CONSUMPTION = 100;
	private final int DEGREE_OF_DEPENDING_OF_FREQUENCY = 2;
	
	@Override
	public float getCurrentConsumptionInMW(){
		float currentFrequency = powerSystemSimulation.getFrequencyInPowerSystem();
		
		return (float)Math.pow((currentFrequency / STANDART_FREQUENCY),
				DEGREE_OF_DEPENDING_OF_FREQUENCY) * CONSTANT_CONSUMPTION;
	}

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulationImpl powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;		
	}
}
