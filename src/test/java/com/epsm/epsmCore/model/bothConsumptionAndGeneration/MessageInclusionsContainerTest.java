package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.dispatch.DispatchingException;

public class MessageInclusionsContainerTest {
	private MessageInclusionsContainer<MessageInclusionImpl> container;
	private MessageInclusionImpl inclusion_1;
	private MessageInclusionImpl inclusion_2;
	private boolean inclusion_1_invoked;
	private boolean inclusion_2_invoked;
	
	@Before
	public void setUp(){
		int powerUnitNumber = 1;
		container = new MessageInclusionsContainer<MessageInclusionImpl > (2);
		inclusion_1 = new MessageInclusionImpl(powerUnitNumber++);
		inclusion_2 = new MessageInclusionImpl(powerUnitNumber++);
	}
	
	private class MessageInclusionImpl extends MessageInclusion{
		public MessageInclusionImpl(int powerUnitNumber) {
			super(powerUnitNumber);
		}

		@Override
		public String toString() {
			if(getInclusionNumber() == 1){
				inclusion_1_invoked = true;
			}else if(getInclusionNumber() == 2){
				inclusion_2_invoked = true;
			}
			
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
		final int samePowerUnitNumber = 1;
		inclusion_1 = new MessageInclusionImpl(samePowerUnitNumber);
		inclusion_2 = new MessageInclusionImpl(samePowerUnitNumber);
		container.addInclusion(inclusion_1);
		container.addInclusion(inclusion_2);
	}
	
	@Test
	public void toStringInvokesForEveryInclusion(){
		int powerUnitNumber = 1;
		inclusion_1 = new MessageInclusionImpl(powerUnitNumber++);
		inclusion_2 = new MessageInclusionImpl(powerUnitNumber++);
		container.addInclusion(inclusion_1);
		container.addInclusion(inclusion_2);
		container.toString();
		
		Assert.assertTrue(inclusion_1_invoked);
		Assert.assertTrue(inclusion_2_invoked);
	}
}
