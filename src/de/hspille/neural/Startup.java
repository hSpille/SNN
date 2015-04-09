package de.hspille.neural;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;

import de.hspille.neural.deepcopy.DeepCopy;

public class Startup {

	// Fitness
	private static final Double X_VALUE = new Double(1.2);
	public static MathContext MATH_CTX = new MathContext(6,RoundingMode.HALF_UP);

	// Genetic Algorithm
	public static double MUTATION_FACTOR = 100;
	private static final int NETWORK_COUNT = 100;
	private static final int BEST_NETS = 5;// NETWORK_COUNT / 2;
	private static final int GENERATIONS = NETWORK_COUNT * 3000;
	private static final int MUTATIONS = NETWORK_COUNT / BEST_NETS;

	// Network Design
	private static final int INPUTS = 1;
	private static final int OUTPUTS = 1;
	private static final int HIDDEN = 12;
	private static final int DEPTH = 3;

	public static void main(String[] args) throws Exception {

		FitnessNetworkComparator f = new FitnessNetworkComparator();
		ArrayList<SimpleNeuralNet> netList = new ArrayList<SimpleNeuralNet>();
		ArrayList<SimpleNeuralNet> bestnetList = new ArrayList<SimpleNeuralNet>();
		createNewNetworks(NETWORK_COUNT, netList);

		for (int i = 0; i < GENERATIONS; i++) {
			// RunTheNetworks
			for (SimpleNeuralNet net : netList) {
				BigDecimal fitness = new BigDecimal(0);
				for (double x = 0; x < Math.PI; x = x + 0.1) {
					BigDecimal sin = new BigDecimal(Math.sin(x));
					BigDecimal returnValue = new BigDecimal(net.doStep(
							new BigDecimal(x)).toString(), MATH_CTX);
					fitness = fitness.add(sin.subtract(returnValue).abs(
							MATH_CTX));
				}
				net.setMyFitness(fitness);
			}
			// FindBestNets
			netList.sort(f);
			BigDecimal bestFitness = netList.get(0).getMyFitness();
//			printNetValue(netList.get(0), i);
			System.out.println("Generation " + i + " best: " + bestFitness);
			if (bestFitness.compareTo(new BigDecimal("0.00004")) < 1) {
				System.out.println("Increasing Mutation factor!");
				MUTATION_FACTOR = MUTATION_FACTOR * 2;
			}
			if (bestFitness.compareTo(new BigDecimal("0.00002")) < 1) {
				System.out.println("Fount a good one!");
				throw new RuntimeException("End");
			}
			if (bestFitness.compareTo(netList.get(BEST_NETS).getMyFitness()) == 0) {
				System.out.println("Best and " + BEST_NETS
						+ ". best have same fitness: ");
				System.out.println("Best  : " + bestFitness);
				System.out.println("Second: " + netList.get(1).getMyFitness());
				throw new RuntimeException("End");
			}
			findBestNets(netList, bestnetList);
			mutateBestNets(netList, bestnetList);

		}

	}

	private static void printNetValue(SimpleNeuralNet simpleNeuralNet,
			int generation) {
		double[] xData = new double[100];
		double[] yData = new double[100];
		double[] zData = new double[100];
		int i = 0;
		for (double x = 0; x < Math.PI; x = x + 0.1) {
			xData[i] = x;
			yData[i] = simpleNeuralNet.doStep(new BigDecimal(x)).doubleValue();
			i = i + 1;
		}

		Chart c = QuickChart.getChart("SNN ".concat("" + generation), "X", "Y",
				"net",xData, yData  );

		try {
			BitmapEncoder.saveBitmap(c, "./Sample_Chart" + generation,
					BitmapFormat.PNG);
			// HighRest
			// BitmapEncoder.saveBitmapWithDPI(c, "./charts", BitmapFormat.PNG,
			// 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void mutateBestNets(ArrayList<SimpleNeuralNet> netList,
			ArrayList<SimpleNeuralNet> bestnetList) {
		netList.clear();
		for (SimpleNeuralNet simpleNeuralNet : bestnetList) {
			for (int i = 0; i < MUTATIONS; i++) {
				SimpleNeuralNet snnCopy = (SimpleNeuralNet) DeepCopy
						.copy(simpleNeuralNet);
				snnCopy.mutate();
				netList.add(snnCopy);
			}
		}
	}

	private static void findBestNets(ArrayList<SimpleNeuralNet> netList,
			ArrayList<SimpleNeuralNet> best) {
		best.clear();
		for (int i = 0; i < BEST_NETS; i++) {
			best.add(netList.get(i));
		}

	}

	private static void createNewNetworks(int total,
			ArrayList<SimpleNeuralNet> netList) {
		for (int i = 0; i < total; i++) {
			SimpleNeuralNet snn = new SimpleNeuralNet();
			snn.buildNet(INPUTS, OUTPUTS, HIDDEN, DEPTH);
			netList.add(snn);
		}
	}

}
