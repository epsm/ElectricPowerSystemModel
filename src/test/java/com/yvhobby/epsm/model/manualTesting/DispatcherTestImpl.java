package test.java.com.yvhobby.epsm.model.manualTesting;

import java.util.Formatter;

import main.java.com.yvhobby.epsm.model.dispatch.ConsumerReport;
import main.java.com.yvhobby.epsm.model.dispatch.Dispatcher;
import main.java.com.yvhobby.epsm.model.dispatch.GeneratorStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationParameters;
import main.java.com.yvhobby.epsm.model.dispatch.PowerStationStateReport;
import main.java.com.yvhobby.epsm.model.dispatch.SimulationReport;

public class DispatcherTestImpl implements Dispatcher{
	
	private String message;
	private StringBuilder sb = new StringBuilder();
	private Formatter fmt = new Formatter(sb);
	
	
	@Override
	public void registerPowerStation(PowerStationParameters parameters) {
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptPowerStationStateReport(PowerStationStateReport PSReport) {
		for(GeneratorStateReport GReport: PSReport.getGeneratorsStatesReports()){
			fmt.format(
					"%12s"+ ", PS=%1d, G=%1d , generation=%6.2f ",
					PSReport.getTimeStamp(), PSReport.getPowerStationNumber(),
					GReport.getGeneratorNumber(), GReport.getGenerationInWM());
		}
		
		sb.append("\n-------------------------------------------------------"
				+ "--------------------------------");
		message =sb.toString();
		sb.setLength(0);
	}
	
	public String getMessage(){
		return message;
	}

	@Override
	public void acceptSimulationParameters(SimulationReport report) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptConsumerReport(ConsumerReport report) {
		// TODO Auto-generated method stub
		
	}
}
