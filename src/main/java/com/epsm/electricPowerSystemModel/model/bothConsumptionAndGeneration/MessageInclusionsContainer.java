package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;

public abstract class MessageInclusionsContainer{
	private int expectedQuantityOfInclusions;
	private Map<Integer, MessageInclusion> inclusions;
	
	public MessageInclusionsContainer(int quantityOfInclusions) {
		if(quantityOfInclusions < 1){
			String message = String.format("%s constructor: quantityOfInclusions must be more than zero,"
					+ " but was %d.", getNameOfThisClass(), quantityOfInclusions);
			throw new DispatchingException(message);
		}
		
		inclusions = new HashMap<Integer, MessageInclusion>();
		expectedQuantityOfInclusions = quantityOfInclusions;
	}

	public final int getQuantityOfInclusions(){
		throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor();
		return expectedQuantityOfInclusions;
	}
	
	private void throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor(){
		if(isContainerKeepWrongInclusionsQuantity()){
			throwWrongInclusionQuantityException();
		}
	}
	
	private boolean isContainerKeepWrongInclusionsQuantity(){
		return expectedQuantityOfInclusions != inclusions.size();
	}
	
	private void throwWrongInclusionQuantityException(){
		String message = String.format("%s keeps %d inclusion(s) but expected %d inclusion(s).",
				getNameOfThisClass(), inclusions.size(), expectedQuantityOfInclusions);
		throw new DispatchingException(message);
	}
	
	private String getNameOfThisClass(){
		return this.getClass().getSimpleName();
	}
	
	public final Set<Integer> getInclusionsNumbers(){
		throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor();
		return Collections.unmodifiableSet(inclusions.keySet());
	}
	
	public final MessageInclusion getInclusion(int inclusionNumber){
		throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor();
		throwExceptionIfRequestedInclusionDoesNotExist(inclusionNumber);
		return inclusions.get(inclusionNumber);
	}
	
	private void throwExceptionIfRequestedInclusionDoesNotExist(int inclusionNumber){
		if(! inclusions.containsKey(inclusionNumber)){
			String message = String.format("%s: there isn't inclusion with number %d, presents only inclusions "
					+ "with numbers: %s.",
					getNameOfThisClass(), inclusionNumber, inclusions.keySet());
			throw new DispatchingException(message);
		}
	}
	
	public final void addInclusion(MessageInclusion inclusion){
		if(inclusion == null){
			String message = String.format("%s addInclusion(...): inclusion can't be null.",
					getNameOfThisClass());
			throw new DispatchingException(message);
		}
		
		int inclusionNumber = inclusion.getInclusionNumber();
		
		if(isInclusionToAddAlreadyInContainer(inclusionNumber)){
			String message = String.format("%s already contain inclusion with this number.",
					getNameOfThisClass());
			throw new DispatchingException(message);
		}

		inclusions.put(inclusionNumber, inclusion);
	}
	
	private boolean isInclusionToAddAlreadyInContainer(int powerUnitNumber){
		return inclusions.containsKey(powerUnitNumber);
	}
	
	@Override
	public String toString(){
		stringBuilder.setLength(0);
		
		for(MessageInclusion inclusion: inclusions.values()){
			stringBuilder.append(inclusion.toString());
		}
		
		return stringBuilder.toString();
	}
}
