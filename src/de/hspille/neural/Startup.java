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
	public static MathContext MATH_CTX = new MathContext(6, RoundingMode.HALF_UP);

	// Genetic Algorithm
	public static double MUTATION_FACTOR = 10;
	private static final int NETWORK_COUNT = 10000;
	private static final int BEST_NETS = 5;//NETWORK_COUNT / 2;
	private static final int GENERATIONS = NETWORK_COUNT * 3000;
	private static final int MUTATIONS = NETWORK_COUNT / BEST_NETS;

	// Network Design
	private static final int INPUTS = 1;
	private static final int OUTPUTS = 1;
	private static final int HIDDEN = 10;
	private static final int DEPTH = 2;

	public static void main(String[] args) throws Exception {

		FitnessNetworkComparator f = new FitnessNetworkComparator();
		ArrayList<SimpleNeuralNet> netList = new ArrayList<SimpleNeuralNet>();
		ArrayList<SimpleNeuralNet> bestnetList = new ArrayList<SimpleNeuralNet>();
		createNewNetworks(NETWORK_COUNT, netList);

		BigDecimal sin = new BigDecimal(Math.sin(X_VALUE));
		for (int i = 0; i < GENERATIONS; i++) {
			calculateNetworFitnessForInput(netList, new BigDecimal(X_VALUE),sin);
			netList.sort(f);
			BigDecimal bestFitness = netList.get(0).getMyFitness();
			System.out.println("Generation " + i + " best: " + bestFitness);
			if(bestFitness.compareTo(new BigDecimal("0.00004")) < 1){
				System.out.println("Increasing Mutation factor!");
				MUTATION_FACTOR = MUTATION_FACTOR * 2;
			}
//			if(bestFitness.compareTo(new BigDecimal("0.000039085967226288431675129686482250690460205078125")) < 1){
			if(bestFitness.compareTo(new BigDecimal("0.00002")) < 1){
				System.out.println("Fount a good one!");
				logSucces(netList, sin);
				throw new RuntimeException("End");
				
			}
			if(bestFitness.compareTo(netList.get(BEST_NETS).getMyFitness()) == 0){
				System.out.println("Best and "+ BEST_NETS  +". best have same fitness: ");
				System.out.println("Best  : " + bestFitness);
				System.out.println("Second: " +netList.get(1).getMyFitness());
				logSucces(bestnetList, sin);
				throw new RuntimeException("End");
			}
//			printNetValue(netList.get(0),i);
			findBestNets(netList, bestnetList);
			mutateBestNets(netList, bestnetList);
		}

	}

	private static void printNetValue(SimpleNeuralNet simpleNeuralNet, int generation) {
		double[] xData = new double[] { 1.1, 1.3};
	    double[] yData = new double[] {simpleNeuralNet.doStep().doubleValue(),simpleNeuralNet.doStep().doubleValue()};
		Chart c =  QuickChart.getChart("SNN ".concat(""+generation), "X", "Y", "y(x)", xData, yData); 
		
		try {
			BitmapEncoder.saveBitmap(c, "./Sample_Chart"+generation, BitmapFormat.PNG);
			//HighRest
//			BitmapEncoder.saveBitmapWithDPI(c, "./charts", BitmapFormat.PNG, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void logSucces(ArrayList<SimpleNeuralNet> netList,
			BigDecimal sin) {
		System.out.println("Net Result: " + netList.get(0).doStep());
		System.out.println("Sin x 1.2 : " + sin);
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

	private static void findBestNets( ArrayList<SimpleNeuralNet> netList, ArrayList<SimpleNeuralNet> best) {
		best.clear();
		for (int i = 0; i < BEST_NETS; i++){
			best.add(netList.get(i));
		}
		
	}

	private static void calculateNetworFitnessForInput(ArrayList<SimpleNeuralNet> netList, BigDecimal input,BigDecimal target) {
		for (SimpleNeuralNet net : netList) {
			net.feedInput(Arrays.asList(input));
			BigDecimal returnValue = new BigDecimal(net.doStep().toString(), MATH_CTX);
			BigDecimal fitness = target.subtract(returnValue).abs(MATH_CTX);
//			System.out.println(" Net Result " + returnValue + " Fitness: " + fitness.floatValue() );
			net.setMyFitness(fitness);
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
