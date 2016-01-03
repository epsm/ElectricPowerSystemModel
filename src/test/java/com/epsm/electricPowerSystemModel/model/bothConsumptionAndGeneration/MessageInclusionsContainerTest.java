package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MessageInclusionsContainerTest {
	private MessageInclusionsContainer container;
	private MessageInclusionImpl inclusion_1;
	private MessageInclusionImpl inclusion_2;
	
	@Before
	public void initialize(){
		container = new MessageInclusionsContainerImpl(1, LocalDateTime.MIN, LocalTime.MIN, 2);
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(2);
	}
	
	private class MessageInclusionsContainerImpl extends MessageInclusionsContainer{
		MessageInclusionsContainerImpl(long powerObjectId, LocalDateTime realTimeStamp, 
				LocalTime simulationTimeStamp, 	int quantityOfPowerUnits){
			
			super(powerObjectId, realTimeStamp, simulationTimeStamp, quantityOfPowerUnits);
		}

		@Override
		public String toString() {
			return null;
		}
	}
	
	private class MessageInclusionImpl extends MessageInclusion{
		public MessageInclusionImpl(int powerUnitNumber) {
			super(powerUnitNumber);
		}
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfQuantityOfInclusionsLessThanOne(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl constructor: "
	    		+ "quantityOfInclusions must be more than zero, but was 0.");
	    
	    container = new MessageInclusionsContainerImpl(0, LocalDateTime.MIN, LocalTime.MIN, 0);
	}
	
	@Test
	public void exceptionInGetQuantityOfInclusionsMethodIfContainerContainsNonExpectedQuantityOfInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl keeps 1 inclusion(s) but expected 2 "
	    		+ "inclusion(s).");
	    
	    addOneInclusionToContainer();
	    container.getQuantityOfInclusions();
	}
	
	private void addOneInclusionToContainer(){
		container.addInclusion(inclusion_1);
	}
	
	@Test
	public void exceptionInGetInclusionsNumbersMethodIfContainerContainsNonExpectedQuantityOfInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl keeps 1 inclusion(s) but expected 2 "
	    		+ "inclusion(s).");
	    
	    addOneInclusionToContainer();
	    container.getInclusionsNumbers();
	}
	
	@Test
	public void exceptionInGetInclusionMethodIfContainerContainsNonExpectedQuantityOfInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl keeps 1 inclusion(s) but expected 2 "
	    		+ "inclusion(s).");
	    
	    addOneInclusionToContainer();
	    container.getInclusion(1);
	}
	
	@Test
	public void exceptionInGetInclusionMesthodIfRequestedInclusionDoesNotExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl: there isn't inclusion with number 3,"
	    		+ " presents only inclusions with numbers: [1, 2]");
	    
	    addTwoInclusionsToContainer();
	    container.getInclusion(3);
	}
	
	private void  addTwoInclusionsToContainer(){
		container.addInclusion(inclusion_1);
		container.addInclusion(inclusion_2);
	}
	
	@Test
	public void doNothinIfGetInclusionMesthodRequestedForExistInclusion(){
	    addTwoInclusionsToContainer();
	    container.getInclusion(2);
	}
	
	@Test
	public void exceptionIfTryToAddNullInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl addInclusion(...): inclusion "
	    		+ "can't be null.");
	    
	    container.addInclusion(null);
	}
	
	@Test
	public void exceptionIfTryToAddInclusionWithTheSameNumberAsExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainerImpl already contain inclusion with this number.");
	    
	    addTwoInclusionsWithTheSameNumbersToContainer();
	}
	
	private void addTwoInclusionsWithTheSameNumbersToContainer(){
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(1);
		container.addInclusion(inclusion_1);
		container.addInclusion(inclusion_2);
	}
}
