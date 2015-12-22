package main.java.com.yvhobby.epsm.model.dispatch;

import java.text.DecimalFormat;
import java.time.LocalTime;

public class ConsumerReport {
	private int consumerNumber;
	private float totalLoad;
	private LocalTime timeStamp;
	private StringBuilder stringBuilder;
	
	public ConsumerReport(int consumerId, float totalLoad, LocalTime timeStamp) {
		this.consumerNumber = consumerId;
		this.totalLoad = totalLoad;
		this.timeStamp = timeStamp;
	}

	public int getConsumerId() {
		return consumerNumber;
	}

	public float getTotalLoad() {
		return totalLoad;
	}

	public LocalTime getTimeStamp() {
		return timeStamp;
	}
	
	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("0000.000");
		
		stringBuilder.setLength(0);
		stringBuilder.append("ConsumerReport ");
		stringBuilder.append("[consumerNumber=");
		stringBuilder.append(consumerNumber);
		stringBuilder.append(", load=");
		stringBuilder.append(formatter.format(totalLoad));
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
