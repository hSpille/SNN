package de.hspille.neural.random;

import java.math.BigDecimal;
import java.util.Random;

import de.hspille.neural.Startup;

public class RandomDeliverer {

	static RandomDeliverer myInstance = null;
	Random r; 
	
	public static RandomDeliverer getInstance(){
		if(myInstance == null){
			myInstance = new RandomDeliverer();
			myInstance.setR(new Random());
			
		}
		return myInstance;
	}
	
	private void setR(Random random) {
		this.r = random;
	}


	public BigDecimal getMutation() {
		double nextDouble = r.nextDouble();
		nextDouble = r.nextInt() < 1 ? nextDouble : nextDouble * -1;
		BigDecimal augend = new BigDecimal(nextDouble / Startup.MUTATION_FACTOR);
		return augend;
	}
	
}
