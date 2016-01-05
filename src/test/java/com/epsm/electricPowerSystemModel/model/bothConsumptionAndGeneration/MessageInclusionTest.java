package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import org.junit.Assert;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MessageInclusionTest {
	MessageInclusion inclusion_1;
	MessageInclusion inclusion_2;
	
	private class MessageInclusionImpl extends MessageInclusion{
		public MessageInclusionImpl(int inclusionNumber) {
			super(inclusionNumber);
		}

		@Override
		public String toString() {
			return null;
		}
	}
	
	@Test
	public void equalsContract() {
	    EqualsVerifier.forClass(MessageInclusion.class)
	    .suppress(Warning.NONFINAL_FIELDS)
	    .verify();
	}
	
	@Test
	public void compareToIfValuesEquals(){
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(1);
		
		Assert.assertEquals(0, inclusion_1.compareTo(inclusion_2));
	}
	
	@Test
	public void compareToIfFirstValueBigger(){
		inclusion_1 = new MessageInclusionImpl(2);
		inclusion_2 = new MessageInclusionImpl(1);
		
		Assert.assertEquals(1, inclusion_1.compareTo(inclusion_2));
	}
	
	@Test
	public void compareToIfSecondValueBigger(){
		inclusion_1 = new MessageInclusionImpl(1);
		inclusion_2 = new MessageInclusionImpl(2);
		
		Assert.assertEquals(-1, inclusion_1.compareTo(inclusion_2));
	}
}
