package com.epsm.electricPowerSystemModel.model.bothConsumptionAndGeneration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingException;
import com.epsm.electricPowerSystemModel.model.dispatch.Parameters;

public class CommandContainerValidatorTest {
	private  Command command;
	private  Parameters parameters;
	private CommandValidator validator;
	HashSet<Long> commandsIds;
    HashSet<Long> parametersIds;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setUp(){
		validator = new CommandContainerValidator(){};
		command = mock(Command.class);
		parameters = mock(Parameters.class);
		when(command.getPowerObjectId()).thenReturn(1L);
		when(parameters.getPowerObjectId()).thenReturn(1L);
		commandsIds = new HashSet<Long>();
	    parametersIds = new HashSet<Long>();
	}
	
	@Test
	public void exceptionIfIdNumbersDifferent(){
		expectedEx.expect(DispatchingException.class);
	    expectedEx.expectMessage("validateOnCommandIdNumbers(...): commands ids [1, 2],"
	    		+ " but parameters ids [2, 3].");
	    
	    commandsIds.add(1L);
	    commandsIds.add(2L);
	    parametersIds.add(2L);
	    parametersIds.add(3L);
	    when(command.getCommandsIds()).thenReturn(commandsIds);
		when(parameters.getCommandsIds()).thenReturn(parametersIds);
		
	    validator.validate(command, parameters);
	}
	
	@Test
	public void noExceptionIfIdNumbersEquals(){
	    commandsIds.add(1L);
	    commandsIds.add(2L);
	    parametersIds.add(1L);
	    parametersIds.add(2L);
	    when(command.getCommandsIds()).thenReturn(commandsIds);
		when(parameters.getCommandsIds()).thenReturn(parametersIds);
		
	    validator.validate(command, parameters);
	}
}
