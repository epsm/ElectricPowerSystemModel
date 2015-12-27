package com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.epsm.electricPowerSystemModel.model.generalModel.GlobalConstants;

public class ConsumerState extends PowerObjectState{
	private int consumerNumber;
	private float load;
	private LocalTime timeStamp;
	private StringBuilder stringBuilder;
	private DateTimeFormatter timeFormatter;
	private DecimalFormat numberFormatter;
	
	public ConsumerState(int consumerNumber, float load, LocalTime timeStamp) {
		this.consumerNumber = consumerNumber;
		this.load = load;
		this.timeStamp = timeStamp;
		
		stringBuilder = new StringBuilder();
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		numberFormatter = new DecimalFormat("000.000", GlobalConstants.SYMBOLS);
	}

	public int getConsumerNumber() {
		return consumerNumber;
	}

	public float getTotalLoad() {
		return load;
	}

	public LocalTime getTimeStamp() {
		return timeStamp;
	}
	
	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("Consumer №");
		stringBuilder.append(consumerNumber);
		stringBuilder.append(" [time: ");
		stringBuilder.append(timeStamp.format(timeFormatter));
		stringBuilder.append(" load MW: ");
		stringBuilder.append(numberFormatter.format(load));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
