package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Dispatcher;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ObjectToBeDispatching;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSenderSource;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstatnts;

public abstract class Consumer implements ObjectToBeDispatching, ReportSenderSource{
	private int number;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	protected ReportSender sender;
	
	public Consumer(int consumerNumber, ElectricPowerSystemSimulation simulation){
		this.number = consumerNumber;
		this.simulation = simulation;
		
		if(simulation == null){
			throw new ConsumptionException("Consumer: simulation must not be null.");
		}
	}

	protected float calculateLoadCountingFrequency(float load, float frequency){
		return (float)Math.pow((frequency / GlobalConstatnts.STANDART_FREQUENCY),
				degreeOnDependingOfFrequency) * load;
	}
	
	protected ConsumerState prepareState(LocalTime timeStamp, float load){
		return new ConsumerState(number, load, timeStamp);
	}
	
	@Override
	public void sendReports(){
		sender.sendReports();
	}
	
	@Override
	public void setReportSender(ReportSender sender) {
		this.sender = sender;
	}

	@Override
	public final void registerWithDispatcher(Dispatcher dispatcher){
		dispatcher.registerPowerObject(this);
	}
	
	public int getConsumerNumber() {
		return number;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
	
	public abstract float calculateCurrentLoadInMW();
}
