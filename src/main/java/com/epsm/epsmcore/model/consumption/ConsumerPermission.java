package com.epsm.epsmcore.model.consumption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerPermission {

	private long powerObjectId;
	private boolean active;
}
