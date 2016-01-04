package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public class CommandValidatorTest {
	private  Command command;
	private  Parameters parameters;
	private CommandValidator validator;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		validator = new CommandValidator(){};
		command = mock(Command.class);
		parameters = mock(Parameters.class);
	}
	
	@Test
	public void exceptionIfCommandIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("validate(...) method: command is null.");
	    
	    command = null;
		
	    validator.validate(command, parameters);
	}
	
	@Test
	public void exceptionIfParametersIsNull(){
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("validate(...) method: parameters is null.");
	    
	    parameters = null;
		
	    validator.validate(command, parameters);
	}
	
	@Test
	public void exceptionIfIdNumbersDifferent(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("validatePowerObjectsId(...): id numbers in command#1 and"
	    		+ " parameters#2 are different.");
	    
	    when(command.getPowerObjectId()).thenReturn(1L);
		when(parameters.getPowerObjectId()).thenReturn(2L);
		
	    validator.validate(command, parameters);
	}
	
	@Test
	public void noExceptionIfIdNumbersEquals(){
		when(command.getPowerObjectId()).thenReturn(1L);
		when(parameters.getPowerObjectId()).thenReturn(1L);
	    
	    validator.validate(command, parameters);
	}
}
