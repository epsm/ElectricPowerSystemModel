package test.java.com.yvaleriy85.esm.model.generation;

import org.junit.Before;
import org.junit.Test;

import main.java.com.yvaleriy85.esm.model.generation.ControlUnit;

public class ControlUnitTest {
	private ControlUnit controlUnit;
	
	@Before
	private void initialize(){
		controlUnit = new ControlUnit();
		controlUnit.setCoefficientOfStatism(4);
		controlUnit.setRequiredFrequency(50);
	}
	
	@Test
	public void staticCharachteristicTest(){
		
	}
	
	
}
