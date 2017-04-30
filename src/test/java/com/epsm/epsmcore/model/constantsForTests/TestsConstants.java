package com.epsm.epsmcore.model.constantsForTests;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

public class TestsConstants {
	public final static List<Float> LOAD_BY_HOURS = asList(
			64.88f,  59.54f,  55.72f,  51.90f, 	48.47f,  48.85f,
			48.09f,  57.25f,  76.35f,  91.60f,  100.0f,  99.23f,
			91.60f,  91.60f,  91.22f,  90.83f,  90.83f,  90.83f,
			90.83f,  90.83f,  90.83f,  90.83f,  90.83f,  83.96f);
	
	public final static LocalDateTime START_DATETIME = LocalDateTime.of(2000, 01, 01, 00, 00);
}
