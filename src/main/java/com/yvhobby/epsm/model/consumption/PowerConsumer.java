package main.java.com.yvhobby.epsm.model.consumption;

import java.time.LocalTime;

import main.java.com.yvhobby.epsm.model.dispatch.ConsumerReport;
import main.java.com.yvhobby.epsm.model.dispatch.Report;
import main.java.com.yvhobby.epsm.model.dispatch.ReportSender;
import main.java.com.yvhobby.epsm.model.dispatch.ReportSenderSource;
import main.java.com.yvhobby.epsm.model.dispatch.ReportSource;
import main.java.com.yvhobby.epsm.model.generalModel.ElectricPowerSystemSimulation;

public abstract class PowerConsumer implements ReportSource, ReportSenderSource{
	private int consumerNumber;
	protected ElectricPowerSystemSimulation simulation;
	protected float degreeOnDependingOfFrequency;
	//childrens must save last load value there for making report without recalculating;
	protected LocalTime currentTime;
	protected float currentLoad;
	protected ReportSender sender;
	private ConsumerReport report;
	
	public PowerConsumer(int consumerNumber){
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

	public void setElectricalPowerSystemSimulation(ElectricPowerSystemSimulation simulation){
		this.simulation = simulation;
	}

	public void setDegreeOfDependingOnFrequency(float degreeOnDependingOfFrequency){
		this.degreeOnDependingOfFrequency = degreeOnDependingOfFrequency;
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
