package main.java.com.epsm.electricPowerSystemModel.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;

public class ConsumerReport extends Report{
	private int consumerNumber;
	private float load;
	private LocalTime timeStamp;
	private StringBuilder stringBuilder;
	private DecimalFormat formatter;
	
	public ConsumerReport(int consumerNumber, float load, LocalTime timeStamp) {
		this.consumerNumber = consumerNumber;
		this.load = load;
		this.timeStamp = timeStamp;
		
		formatter = new DecimalFormat("0000.000");
	}

	public int getConsumerId() {
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
		stringBuilder.append("ConsumerReport ");
		stringBuilder.append("[consumerNumber=");
		stringBuilder.append(consumerNumber);
		stringBuilder.append(", load=");
		stringBuilder.append(formatter.format(load));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
