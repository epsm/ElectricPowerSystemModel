package com.epsm.electricPowerSystemModel.model.dispatch;

import java.time.LocalDateTime;

public abstract class DispatcherMessage {
	private LocalDateTime creationTime;

	public DispatcherMessage(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}
}
