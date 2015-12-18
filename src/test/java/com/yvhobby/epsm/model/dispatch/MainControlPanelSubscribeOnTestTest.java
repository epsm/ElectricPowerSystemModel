package test.java.com.yvhobby.epsm.model.dispatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.MainControlPanel;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationStateReport;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.Generator;
import main.java.com.yvhobby.epsm.model.generation.PowerStation;

public class MainControlPanelSubscribeOnTestTest {
	private ArgumentCaptor<PowerStationStateReport> stationStateReportCaptor;
	private ElectricPowerSystemSimulation simulation;
	private Dispatcher dispatcher;
	private MainControlPanel stationControlPanel;
	private PowerStationStateReport stationStateReport;
	private LocalTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalTime.NOON;
	private final int STATION_ID = 158;
	
	@Before
	public void initialize(){
		stationStateReportCaptor = ArgumentCaptor.forClass(PowerStationStateReport.class);
		simulation = mock(ElectricPowerSystemSimulation.class);
		dispatcher = mock(Dispatcher.class);
		stationControlPanel = new MainControlPanel();
		PowerStation station = new PowerStation();
		Generator generator_1 = mock(Generator.class);
		Generator generator_2 = mock(Generator.class);
		
		when(generator_1.getId()).thenReturn(1);
		when(generator_1.isTurnedOn()).thenReturn(true);
		when(generator_1.getGenerationInMW()).thenReturn(100f);
		when(generator_2.getId()).thenReturn(2);
		when(generator_2.isTurnedOn()).thenReturn(true);
		when(generator_2.getGenerationInMW()).thenReturn(200f);
		when(simulation.getTime()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		
		stationControlPanel.setSimulation(simulation);
		stationControlPanel.setDispatcher(dispatcher);
		stationControlPanel.setStation(station);
		station.setId(STATION_ID);
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
	}
	
	@Test
	public void stationSendsCorrectReportsToDispatcher() throws InterruptedException {
		boolean firstGeneratorTurnedOn = false;
		boolean secondGeneratorTurnedOn = false;
		float firstGeneratorGeneration = 0;
		float secondGeneratorGeneration = 0;
		
		stationControlPanel.subscribeOnReports();
		doPauseUntilFirstStateReportBeTransferedToDispatcher();
		obtainReportFromDispatcher();
		
		for(GeneratorStateReport generatorStateReport: stationStateReport.getGeneratorsStatesReports()){
			if(generatorStateReport.getGeneratorId() == 1){
				firstGeneratorTurnedOn = generatorStateReport.isTurnedOn();
				firstGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}else if(generatorStateReport.getGeneratorId() == 2){
				secondGeneratorTurnedOn = generatorStateReport.isTurnedOn();
				secondGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}
		}
		
		Assert.assertEquals(2, stationStateReport.getGeneratorsStatesReports().size());
		Assert.assertEquals(STATION_ID, stationStateReport.getPowerStationId());
		Assert.assertEquals(CONSTANT_TIME_IN_MOCK_SIMULATION, stationStateReport.getTimeStamp());
		Assert.assertTrue(firstGeneratorTurnedOn);
		Assert.assertTrue(secondGeneratorTurnedOn);
		Assert.assertEquals(100, firstGeneratorGeneration, 0);
		Assert.assertEquals(200, secondGeneratorGeneration, 0);
	}
	
	private void obtainReportFromDispatcher(){
		verify(dispatcher).acceptPowerStationStateReport(stationStateReportCaptor.capture());
		stationStateReport = stationStateReportCaptor.getValue();
	}
	
	private void doPauseUntilFirstStateReportBeTransferedToDispatcher() throws InterruptedException{
		Thread.sleep(300);
	}
	
	@Test
	public void stationSendsRequestsEverySecond() throws InterruptedException{
		stationControlPanel.subscribeOnReports();
		doPauseUntilStateReportBeTransferedToDispatcherTwice();
		hasReportBeenSentTwice();
	}
	
	private void doPauseUntilStateReportBeTransferedToDispatcherTwice() throws InterruptedException{
		Thread.sleep((long)(GlobalConstatnts.PAUSE_BETWEEN_STATE_REPORTS_TRANSFERS_IN_MILLISECONDS * 1.3));
	}
	
	private void hasReportBeenSentTwice(){
		verify(dispatcher, times(2)).acceptPowerStationStateReport(any());
	}
}