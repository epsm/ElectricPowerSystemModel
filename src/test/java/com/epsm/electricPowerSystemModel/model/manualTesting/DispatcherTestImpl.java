package test.java.com.epsm.electricPowerSystemModel.model.manualTesting;

import java.util.Formatter;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ConsumerReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.GeneratorStateReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationParameters;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerStationReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Report;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.SimulationReport;

/*public class DispatcherTestImpl implements Dispatcher{
	
	private String message;
	private StringBuilder sb = new StringBuilder();
	private Formatter fmt = new Formatter(sb);
	
	
	@Override
	public void registerPowerStation(PowerStationParameters parameters) {
		// TODO Auto-generated method stub
	}

	@Override
	public void acceptReport(Report PSReport) {
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
}*/