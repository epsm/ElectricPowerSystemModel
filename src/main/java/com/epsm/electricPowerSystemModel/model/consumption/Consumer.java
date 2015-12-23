package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ConsumerState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.PowerObjectState;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSenderSource;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ObjectToBeDispatching;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public abstract class Consumer implements ObjectToBeDispatching, ReportSenderSource{
	private int consumerNumber;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	protected LocalTime currentTime;
	protected float currentLoad;
	protected ReportSender sender;
	private ConsumerState report;
	
	public Consumer(int consumerNumber){
		this.consumerNumber = consumerNumber;
	}

	@Override
	public void sendReports(){
		sender.sendReports();
	}
	
	@Override
	public PowerObjectState getState() {
		prepareState();
		
		return report;
	}
	
	private void prepareState(){
		LocalTime timeStamp = simulation.getTime();
		report = new ConsumerState(consumerNumber, currentLoad, timeStamp);
	}
	
	public int getConsumerNumber() {
		return consumerNumber;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
	}
	
	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation simulation){
		this.simulation = simulation;
	}

	public float getDegreeOnDependingOfFrequency() {
		return degreeOnDependingOfFrequency;
	}
	
	@Override
	public void setReportSender(ReportSender sender) {
		this.sender = sender;
	}

	public abstract float calculateCurrentLoadInMW();
}
