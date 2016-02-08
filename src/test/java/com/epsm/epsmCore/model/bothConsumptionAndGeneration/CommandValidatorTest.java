package com.epsm.epsmCore.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.epsmCore.model.dispatch.Command;
import com.epsm.epsmCore.model.dispatch.DispatchingException;
import com.epsm.epsmCore.model.dispatch.Parameters;

public class CommandValidatorTest {
	private  Command command;
	private  Parameters parameters;
	private CommandValidator validator;
	private final long FIRST_POWER_OBJECT_ID = 1;
	private final long SECOND_POWER_OBJECT_ID = 2;
	
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
	    
	    when(command.getPowerObjectId()).thenReturn(FIRST_POWER_OBJECT_ID);
		when(parameters.getPowerObjectId()).thenReturn(SECOND_POWER_OBJECT_ID);
		
	    validator.validate(command, parameters);
	}
	
	@Test
	public void noExceptionIfIdNumbersEquals(){
		when(command.getPowerObjectId()).thenReturn(FIRST_POWER_OBJECT_ID);
		when(parameters.getPowerObjectId()).thenReturn(FIRST_POWER_OBJECT_ID);
	    
	    validator.validate(command, parameters);
	}
}
