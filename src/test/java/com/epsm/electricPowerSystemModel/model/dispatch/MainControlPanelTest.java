package test.java.com.epsm.electricPowerSystemModel.model.dispatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorStateReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.MainControlPanel;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Report;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generation.Generator;
import main.java.com.epsm.electricPowerSystemModel.model.generation.PowerStation;

public class MainControlPanelTest{
	private ElectricPowerSystemSimulation simulation;
	private PowerStation station;
	private MainControlPanel stationControlPanel;
	private PowerStationReport stationStateReport;
	private LocalTime CONSTANT_TIME_IN_MOCK_SIMULATION = LocalTime.NOON;
	private final int STATION_NUMBER = 158;
	private final float FIRST_GENERATOR_GENERATION = 100;
	private final float SECOND_GENERATOR_GENERATION = 200;
	
	@Before
	public void initialize(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		station = new PowerStation(STATION_NUMBER);
		stationControlPanel = new MainControlPanel();
		Generator generator_1 = mock(Generator.class);
		Generator generator_2 = mock(Generator.class);
		
		when(generator_1.getNumber()).thenReturn(1);
		when(generator_1.isTurnedOn()).thenReturn(true);
		when(generator_1.getGenerationInMW()).thenReturn(FIRST_GENERATOR_GENERATION);
		when(generator_2.getNumber()).thenReturn(2);
		when(generator_2.isTurnedOn()).thenReturn(true);
		when(generator_2.getGenerationInMW()).thenReturn(SECOND_GENERATOR_GENERATION);
		when(simulation.getTime()).thenReturn(CONSTANT_TIME_IN_MOCK_SIMULATION);
		
		stationControlPanel.setSimulation(simulation);
		stationControlPanel.setStation(station);
		station.addGenerator(generator_1);
		station.addGenerator(generator_2);
	}
	
	@Test
	public void controlPanelMakesRightReports(){
		calculateOneStepInSimulation();
		getStationReport();
		verifyStationReport();
	}

	private void calculateOneStepInSimulation(){
		station.calculateGenerationInMW();
	}
	
	private void getStationReport(){
		Report rawReport = stationControlPanel.getReport();
		
		Assert.assertTrue(rawReport instanceof PowerStationReport);
		
		stationStateReport = (PowerStationReport) rawReport;
	}
	
	private void verifyStationReport(){
		int firstGeneratorNumber = 0;
		int secondtGeneratorNumber = 0;
		float firstGeneratorGeneration = 0;
		float secondGeneratorGeneration = 0;
		
		for(GeneratorStateReport generatorStateReport: stationStateReport.getGeneratorsStatesReports()){
			if(generatorStateReport.getGeneratorNumber() == 1){
				firstGeneratorGeneration = generatorStateReport.getGenerationInWM();
				firstGeneratorNumber = generatorStateReport.getGeneratorNumber();
			}else if(generatorStateReport.getGeneratorNumber() == 2){
				secondtGeneratorNumber = generatorStateReport.getGeneratorNumber();
				secondGeneratorGeneration = generatorStateReport.getGenerationInWM();
			}
		}
		
		Assert.assertEquals(2, stationStateReport.getGeneratorsStatesReports().size());
		Assert.assertEquals(STATION_NUMBER, stationStateReport.getPowerStationNumber());
		Assert.assertEquals(CONSTANT_TIME_IN_MOCK_SIMULATION, stationStateReport.getTimeStamp());
		Assert.assertEquals(1, firstGeneratorNumber);
		Assert.assertEquals(2, secondtGeneratorNumber);
		Assert.assertEquals(FIRST_GENERATOR_GENERATION, firstGeneratorGeneration, 0);
		Assert.assertEquals(SECOND_GENERATOR_GENERATION, secondGeneratorGeneration, 0);
	}
}