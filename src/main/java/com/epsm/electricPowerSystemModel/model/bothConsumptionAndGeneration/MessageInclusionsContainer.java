package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;

public class MessageInclusionsContainer<E extends MessageInclusion>{
	private int expectedQuantityOfInclusions;
	private Map<Integer, E> inclusions;
	protected StringBuilder stringBuilder;
	
	public MessageInclusionsContainer(int quantityOfInclusions) {
		if(quantityOfInclusions < 1){
			String message = String.format("MessageInclusionsContainer constructor: "
					+ "quantityOfInclusions must be more than zero, but was %d."
					, quantityOfInclusions);
			throw new IllegalArgumentException(message);
		}
		
		inclusions = new TreeMap<Integer, E>();
		expectedQuantityOfInclusions = quantityOfInclusions;
		stringBuilder = new StringBuilder();
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
		String message = String.format("MessageInclusionsContainer keeps %d inclusion(s) but"
				+ " expected %d inclusion(s).", inclusions.size()
				, expectedQuantityOfInclusions);
		throw new DispatchingException(message);
	}
	
	public final Set<Integer> getInclusionsNumbers(){
		throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor();
		return Collections.unmodifiableSet(inclusions.keySet());
	}
	
	public final E getInclusion(int inclusionNumber){
		throwExceptionIfQuantityOfInclusionsNotAsDefinedInConstructor();
		throwExceptionIfRequestedInclusionDoesNotExist(inclusionNumber);
		return inclusions.get(inclusionNumber);
	}
	
	private void throwExceptionIfRequestedInclusionDoesNotExist(int inclusionNumber){
		if(! inclusions.containsKey(inclusionNumber)){
			String message = String.format("MessageInclusionsContainer: there isn't inclusion"
					+ " with number %d, presents only inclusions with numbers: %s."
					, inclusionNumber, inclusions.keySet());
			throw new DispatchingException(message);
		}
	}
	
	public final void addInclusion(E inclusion){
		if(inclusion == null){
			String message = "MessageInclusionsContainer addInclusion(...): inclusion can't "
					+ "be null.";
			throw new IllegalArgumentException(message);
		}
		
		int inclusionNumber = inclusion.getInclusionNumber();
		
		if(isInclusionToAddAlreadyInContainer(inclusionNumber)){
			String message = "MessageInclusionsContainer already contain inclusion with this"
					+ " number.";
			throw new DispatchingException(message);
		}

		inclusions.put(inclusionNumber, inclusion);
	}
	
	private boolean isInclusionToAddAlreadyInContainer(int powerUnitNumber){
		return inclusions.containsKey(powerUnitNumber);
	}
	
	@Override
	public String toString(){
		for(MessageInclusion inclusion: inclusions.values()){
			stringBuilder.append(inclusion.toString());
		}
		
		return stringBuilder.toString();
	}
}
