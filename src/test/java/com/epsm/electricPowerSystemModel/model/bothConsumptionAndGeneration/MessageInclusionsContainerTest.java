package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;

public class MessageInclusionsContainerTest {
	private MessageInclusionsContainer<MessageInclusionImpl> container;
	private MessageInclusionImpl inclusion_1;
	private MessageInclusionImpl inclusion_2;
	
	@Before
	public void setUp(){
		container = new MessageInclusionsContainer<MessageInclusionImpl>(2);
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(2);
	}
	
	private class MessageInclusionImpl extends MessageInclusion{
		public MessageInclusionImpl(int powerUnitNumber) {
			super(powerUnitNumber);
		}

		@Override
		public String toString() {
			return null;
		}
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void exceptionInConstructorIfQuantityOfInclusionsLessThanOne(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer constructor: "
	    		+ "quantityOfInclusions must be more than zero, but was 0.");
	    
	    container = new MessageInclusionsContainer<MessageInclusionImpl>(0);
	}
	
	@Test
	public void exceptionInGetQuantityOfInclusionsMethodIfContainerContainsNonExpectedQuantityOfInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer keeps 1 inclusion(s) but expected 2 "
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
	    expectedEx.expectMessage("MessageInclusionsContainer keeps 1 inclusion(s) but expected 2 "
	    		+ "inclusion(s).");
	    
	    addOneInclusionToContainer();
	    container.getInclusionsNumbers();
	}
	
	@Test
	public void exceptionInGetInclusionMethodIfContainerContainsNonExpectedQuantityOfInclusion(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer keeps 1 inclusion(s) but expected 2 "
	    		+ "inclusion(s).");
	    
	    addOneInclusionToContainer();
	    container.getInclusion(1);
	}
	
	@Test
	public void exceptionInGetInclusionMesthodIfRequestedInclusionDoesNotExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer: there isn't inclusion with number 3,"
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
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer addInclusion(...): inclusion "
	    		+ "can't be null.");
	    
	    container.addInclusion(null);
	}
	
	@Test
	public void exceptionIfTryToAddInclusionWithTheSameNumberAsExist(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("MessageInclusionsContainer already contain inclusion with this number.");
	    
	    addTwoInclusionsWithTheSameNumbersToContainer();
	}
	
	private void addTwoInclusionsWithTheSameNumbersToContainer(){
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(1);
		container.addInclusion(inclusion_1);
		container.addInclusion(inclusion_2);
	}
}
