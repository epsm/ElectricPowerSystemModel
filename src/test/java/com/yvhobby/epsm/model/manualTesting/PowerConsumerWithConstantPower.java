package test.java.com.yvhobby.epsm.model.manualTesting;

import main.java.com.yvhobby.epsm.model.consumption.PowerConsumer;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;

public class PowerConsumerWithConstantPower extends PowerConsumer{
	private ElectricPowerSystemSimulation powerSystemSimulation;
	private final int CONSTANT_CONSUMPTION = 100;
	private final int DEGREE_OF_DEPENDING_OF_FREQUENCY = 2;
	
	@Override
	public float getCurrentConsumptionInMW(){
		float currentFrequency = powerSystemSimulation.getFrequencyInPowerSystem();
		
		return (float)Math.pow((currentFrequency / GlobalConstatnts.STANDART_FREQUENCY),
				DEGREE_OF_DEPENDING_OF_FREQUENCY) * CONSTANT_CONSUMPTION;
	}

	@Override
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation powerSystemSimulation) {
		this.powerSystemSimulation = powerSystemSimulation;		
	}
}
