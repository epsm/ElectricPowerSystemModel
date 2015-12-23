package main.java.com.epsm.electricPowerSystemModel.model.consumption;

import java.time.LocalTime;

import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ConsumerReport;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.Report;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSender;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSenderSource;
import main.java.com.epsm.electricPowerSystemModel.model.dispatch.ReportSource;
import main.java.com.epsm.electricPowerSystemModel.model.generalModel.ElectricPowerSystemSimulation;

public abstract class Consumer implements ReportSource, ReportSenderSource{
	private int consumerNumber;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	protected LocalTime currentTime;
	protected float currentLoad;
	protected ReportSender sender;
	private ConsumerReport report;
	
	public Consumer(int consumerNumber){
		this.consumerNumber = consumerNumber;
	}

	@Override
	public void subscribeOnReports(){
		sender.sendReports();
	}
	
	@Override
	public Report getReport() {
		prepareReport();
		
		return report;
	}
	
	private void prepareReport(){
		LocalTime timeStamp = simulation.getTime();
		report = new ConsumerReport(consumerNumber, currentLoad, timeStamp);
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
