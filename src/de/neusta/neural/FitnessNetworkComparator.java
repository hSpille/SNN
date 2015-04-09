package de.neusta.neural;

import java.math.BigDecimal;
import java.util.Comparator;

public class FitnessNetworkComparator implements Comparator<SimpleNeuralNet> {

	@Override
	public int compare(SimpleNeuralNet o1, SimpleNeuralNet o2) {
		BigDecimal fitness01 = o1.getMyFitness();
		BigDecimal fitness02 = o2.getMyFitness();
		return fitness01.compareTo(fitness02);
	}
}
