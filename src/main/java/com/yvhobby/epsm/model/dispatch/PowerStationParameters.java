package main.java.com.yvhobby.epsm.model.dispatch;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PowerStationParameters{
	private int powerStationNumber;
	private Map<Integer, GeneratorParameters> generatorParameters;
	
	public PowerStationParameters(int powerStationNumber, Map<Integer, GeneratorParameters> generatorsParameters) {
		this.powerStationNumber = powerStationNumber;
		this.generatorParameters = Collections.unmodifiableMap(generatorsParameters);
	}

	public GeneratorParameters getGeneratorParameters(int generatorNumber){
		return generatorParameters.get(generatorNumber);
	}

	public int getPowerStationNumber() {
		return powerStationNumber;
	}
	
	public int getQuantityOfGenerators(){
		return generatorParameters.size();
	}
	
	public Collection<Integer> getGeneratorsNumbers(){
		return generatorParameters.keySet();
	}
}
