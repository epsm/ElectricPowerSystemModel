package com.epsm.epsmCore.model.utils.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epsm.epsmCore.model.generation.GeneratorState;
import com.epsm.epsmCore.model.generation.PowerStationState;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class PowerStationStateJsonDeserializer extends JsonDeserializer<PowerStationState>{

	private PowerStationState stationState;
	private GeneratorState generatorState;
	private List<GeneratorState> generatorStatesList = new ArrayList<GeneratorState>();
	private long powerObjectId;
	private LocalDateTime realTimeStamp;
	private LocalDateTime simulationTimeStamp;
	private float frequency;
	private int generatorQuantity;
	private int generatorNumber;
	private float generationInWM;

	private Logger logger = LoggerFactory.getLogger(PowerStationStateJsonDeserializer.class);
	
	@Override
	public PowerStationState deserialize(JsonParser jParser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		resetState();
		parseJson(jParser);
		createPowerStationState();
		addGeneratorStates();
		logger.debug("Deserialized: {}." + stationState);
		
		return stationState;
	}
	
	private void resetState(){
		generatorStatesList.clear();
	}
	
	private void parseJson(JsonParser jParser) throws JsonParseException, IOException{
		while(jParser.nextToken() != JsonToken.END_OBJECT){
			String name = jParser.getCurrentName();
			
			if("powerObjectId".equals(name)){
				jParser.nextToken();
				powerObjectId = jParser.getLongValue();
			}else if("realTimeStamp".equals(name)){
				jParser.nextToken();
				realTimeStamp = LocalDateTime.parse(jParser.getText());
			}else if("simulationTimeStamp".equals(name)){
				jParser.nextToken();
				simulationTimeStamp = LocalDateTime.parse(jParser.getText());
			}else if("generatorQuantity".equals(name)){
				jParser.nextToken();
				generatorQuantity = jParser.getIntValue();
			}else if("frequency".equals(name)){
				jParser.nextToken();
				frequency = jParser.getFloatValue();
			}else if("generators".equals(name)){
				jParser.nextToken();
				while(jParser.nextToken() != JsonToken.END_OBJECT){
					parseJson(jParser);
					generatorState = new GeneratorState(generatorNumber, generationInWM);
					generatorStatesList.add(generatorState);
				}
			}else if("generationInWM".equals(name)){
				jParser.nextToken();
				generationInWM = jParser.getFloatValue();
			}else if("generatorNumber".equals(name)){
				jParser.nextToken();
				generatorNumber = jParser.getIntValue();
			}
		}
	}
	
	private void createPowerStationState(){
		stationState = new PowerStationState(powerObjectId, realTimeStamp, simulationTimeStamp,
				generatorQuantity, frequency);
	}
	
	private void addGeneratorStates(){
		for(GeneratorState state: generatorStatesList){
			stationState.addGeneratorState(state);
		}
	}
}
