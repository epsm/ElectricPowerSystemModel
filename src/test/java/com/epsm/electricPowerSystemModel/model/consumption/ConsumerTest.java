package test.java.com.epsm.electricPowerSystemModel.model.consumption;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import main.java.com.epsm.electricPowerSystemModel.model.consumption.Consumer;
import main.java.com.epsm.electricPowerSystemModel.model.consumption.ScheduledLoadConsumer;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;

public class ConsumerTest {
	private Dispatcher dispatcher;
	private Consumer consumer;
	
	@Before
	public void initialize(){
		dispatcher = mock(Dispatcher.class);
		
		//as method setDispatcher(...) defined in Consumer as final I can test only one any implementation 
		consumer = new ScheduledLoadConsumer(1, null);
	}
	
	@Test
	public void consumerRegisteresWithDispacherRightAfterSetIt(){
		consumer.registerWithDispatcher(dispatcher);
		
		verify(dispatcher).registerPowerObject(any());
	}
}
