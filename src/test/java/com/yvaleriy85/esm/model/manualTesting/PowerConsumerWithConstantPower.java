package test.java.com.yvaleriy85.esm.model.manualTesting;

import main.java.com.yvaleriy85.esm.model.ElectricPowerSystemSimulation;
import main.java.com.yvaleriy85.esm.model.PowerConsumer;

public class PowerConsumerWithConstantPower extends PowerConsumer{
	private ElectricPowerSystemSimulation powerSystemSimulation;
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
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;		
	}
}
