package main.java.com.yvaleriy85.esm.model;

import java.util.ArrayList;
import java.util.List;

public class PowerStation{
	
	private List<Generator> generators = new ArrayList<Generator>();
	
	public float getCurrentGenerationInMW(){
		float generationInMW = 0;
		
		for(Generator generator: generators){
			generationInMW += generator.getGenerationInMW();
		}
		
		return generationInMW;
	}
	
	public void addGenerator(Generator generator){
		generators.add(generator);
	}
}
