package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DispatcherMessage {
	private LocalDateTime creationTime;
	private StringBuilder stringBuilder;
	private DateTimeFormatter timeFormatter;

	public DispatcherMessage(LocalDateTime creationTime) {
		super();
		this.creationTime = creationTime;
		
		stringBuilder = new StringBuilder();
		timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	@Override
	public String toString() {
		stringBuilder.setLength(0);
		stringBuilder.append("Message creation time: ");
		stringBuilder.append(creationTime.format(timeFormatter));
		stringBuilder.append(".");
		
		return stringBuilder.toString();
	}
}
