package test.java.com.yvhobby.epsm.model.generation;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.yvhobby.epsm.model.generalModel.GlobalConstatnts;
import main.java.com.yvhobby.epsm.model.generation.AstaticRegulatioUnit;
import main.java.com.yvhobby.epsm.model.generation.ControlUnit;
import main.java.com.yvhobby.epsm.model.generation.Generator;

public class ControlUnitTest {
	private ElectricPowerSystemSimulation simulation;
	private Generator generator;
	private ControlUnit controlUnit;
	private AstaticRegulatioUnit regulationUnit;
	private float powerForGenerator;
	
	@Before
	public void init(){
		generator = new Generator();
		controlUnit = new ControlUnit();
		powerForGenerator = 100;
		
		controlUnit.setGenerator(generator);
		controlUnit.setCoefficientOfStatism(0.1f);
		controlUnit.setPowerAtRequiredFrequency(powerForGenerator);
		controlUnit.setRequiredFrequency(GlobalConstatnts.STANDART_FREQUENCY);
		
		generator.setMinimalTechnologyPower(50);
		generator.setNominalPowerInMW(150);
	}

	@Test
	public void PowerIncreasesWhenFrequencyIsLow(){
		prepareMockSimulationWithLowFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(controlUnit.getGeneratorPowerInMW() > powerForGenerator);
		}
	}
	
	private void prepareMockSimulationWithLowFrequency(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(49.9f).
				thenReturn(49f).thenReturn(40f);
		controlUnit.setElectricPowerSystemSimulation(simulation);
	}
	
	@Test
	public void PowerDecreasesWhenFrequencyIsHight(){
		prepareMockSimulationWithHighFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(controlUnit.getGeneratorPowerInMW() < powerForGenerator);
		}
	}
	
	private void prepareMockSimulationWithHighFrequency(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(50.1f).
				thenReturn(55f).thenReturn(60f);
		controlUnit.setElectricPowerSystemSimulation(simulation);
	}
	
	@Test
	public void PowerIsEqualsToPowerAtRequiredFrequencyWhenFrequencyIsEqualToRequired(){
		prepareMockSimulationWithNormalFrequency();
		
		for(int i = 0; i < 3; i++){
			Assert.assertTrue(controlUnit.getGeneratorPowerInMW() == powerForGenerator);
		}
	}
	
	private void prepareMockSimulationWithNormalFrequency(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(50.0f);
		controlUnit.setElectricPowerSystemSimulation(simulation);
	}
	
	@Test
	public void DoesAstacicRegulationUnitMethodCallIItTurnedOn(){
		prepareMockSimulationAndAstaticRegulationUnit();
		
		controlUnit.TurnOnAstaticRegulation();
		controlUnit.getGeneratorPowerInMW();
		
		verify(regulationUnit, times(1)).verifyAndAdjustPowerAtRequiredFrequency(0f);
	}
	
	private void prepareMockSimulationAndAstaticRegulationUnit(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		regulationUnit = mock(AstaticRegulatioUnit.class);
		controlUnit.setElectricPowerSystemSimulation(simulation);
		controlUnit.setAstaticRegulationUnit(regulationUnit);
	}
	
	@Test 
	public void DoesPowerNotLessThanGeneratorMinimalTechnilogyPower(){
		prepareMockSimulationWithTooHightFrequency();
		
		Assert.assertTrue(controlUnit.getGeneratorPowerInMW() >= generator.getMinimalTechnologyPower());
	}
	
	private void prepareMockSimulationWithTooHightFrequency(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(1000f);
		controlUnit.setElectricPowerSystemSimulation(simulation);
	}
	
	@Test 
	public void DoesPowerNotHigherThanGeneratorNomimalPower(){
		prepareMockSimulationWithTooLowtFrequency();
			
		Assert.assertTrue(controlUnit.getGeneratorPowerInMW() <= generator.getNominalPowerInMW());
	}
	
	private void prepareMockSimulationWithTooLowtFrequency(){
		simulation = mock(ElectricPowerSystemSimulation.class);
		when(simulation.getFrequencyInPowerSystem()).thenReturn(0.00000000001f);
		controlUnit.setElectricPowerSystemSimulation(simulation);
	}
}
