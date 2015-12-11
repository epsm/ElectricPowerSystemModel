package main.java.com.yvaleriy85.esm.model;

public class PowerConsumerWithConstantPower extends PowerConsumer{

	private final float STANDART_FREQUENCY = 50;
	private final int CONSTANT_CONSUMPTION = 100;
	private final int DEGREE_OF_DEPENDING_OF_FREQUENCY = 2;
	
	@Override
	public float getCurrentConsumptionInMW(){
		float currentFrequency = EnergySystem.getFrequencyInPowerSystem();
		
		return (float)Math.pow((currentFrequency / STANDART_FREQUENCY),
				DEGREE_OF_DEPENDING_OF_FREQUENCY) * CONSTANT_CONSUMPTION;
	}

}
