package main.java.com.yvaleriy85.esm.model.consumption;

import java.time.LocalTime;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import main.java.com.yvaleriy85.esm.model.generalModel.GlobalConstatnts;

public class ConsumptionSchedule{
	private PolynomialSplineFunction dayConsumptionSchedule;
	
	public ConsumptionSchedule(PolynomialSplineFunction dayConsumptionSchedule) {
		this.dayConsumptionSchedule = dayConsumptionSchedule;
	}
	
	public float getConsumptionOnTime(LocalTime time){
		double valueForRequest = convertDayTimeToDecimalValue(time);
		
		try{
			return (float)dayConsumptionSchedule.value(valueForRequest);
		}catch(Exception e){
			System.out.println("value for request = " + valueForRequest);
			System.out.println("double = " + (24d * time.toNanoOfDay() / GlobalConstatnts.NANOS_IN_DAY));
			throw e;
		}
	}
	
	private double convertDayTimeToDecimalValue(LocalTime time){
		//System.out.println("nanos of day = " + time.toNanoOfDay());
		return 24d * time.toNanoOfDay() / GlobalConstatnts.NANOS_IN_DAY;
	}
}
