package com.epsm.electricPowerSystemModel.service;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epsm.electricPowerSystemModel.model.consumption.ConsumptionPermissionStub;
import com.epsm.electricPowerSystemModel.model.dispatch.Command;
import com.epsm.electricPowerSystemModel.model.dispatch.DispatchingObject;
import com.epsm.electricPowerSystemModel.model.generalModel.DispatchingObjectsSource;

@RunWith(MockitoJUnitRunner.class)
public class IncomingMessageServiceImplTest {
	private Command command;
	
	@InjectMocks
	private IncomingMessageServiceImpl service;
	
	@Mock
	private DispatchingObjectsSource source;
	
	@Mock
	private DispatchingObject object;
	
	@Before
	public void setUp(){
		Map<Long,DispatchingObject> dispatchingObjects = new HashMap<Long,DispatchingObject>();
		dispatchingObjects.put(1L, object);
		when(source.getDispatchingObjects()).thenReturn(dispatchingObjects);
		command = new ConsumptionPermissionStub(1, LocalDateTime.MIN, LocalTime.MIN);
	}
	
	@Test
	public void requestsDispatchingObjectsWhenCommandReceived(){
		service.acceptCommand(command);
		
		verify(source).getDispatchingObjects();
	}
	
	@Test
	public void passesComandToObject(){
		service.acceptCommand(command);
		
		verify(object).executeCommand(command);
	}
	
	@Test
	public void doNothingIfCommadIsNull(){
		service.acceptCommand(null);
		
		verify(source, never()).getDispatchingObjects();
	}
	
	@Test
	public void doNothingIfCommandAdressedToNonExistsObject(){
		command = new ConsumptionPermissionStub(99999, LocalDateTime.MIN, LocalTime.MIN);
		service.acceptCommand(command);
	}
}
