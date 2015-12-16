package main.java.com.yvhobby.epsm.model.generation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PowerStation{
	
	private int id;
	private Set<Generator> generators = new HashSet<Generator>();
	
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
	
	public Set<Generator> getGenerators(){
		return Collections.unmodifiableSet(generators);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
