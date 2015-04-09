package de.hspille.neural;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.hspille.neural.random.RandomDeliverer;

public class Neuron implements Serializable {

	private static final long serialVersionUID = 4153551042514448208L;
	private List<Input> inputs = new ArrayList<Input>();
	private List<Neuron> inputNeurons = new ArrayList<Neuron>();
	private List<BigDecimal> weights = new ArrayList<BigDecimal>();
	private boolean isOutputNeuron = false;
	public Neuron() {
		
	}

	/**
	 * kind or recursive
	 * 
	 * @return
	 */
	public BigDecimal calculateOutPut() {
		BigDecimal activation = new BigDecimal(0);
		for (Input inputNeuron : inputs) {
			activation = activation.add(inputNeuron.calculateOutput());
		}
		for (Neuron neuron : inputNeurons) {
			BigDecimal out = weights.get(inputNeurons.indexOf(neuron)).multiply(
					neuron.calculateOutPut());
			activation = activation.add(out);
		}
		if(!isOutputNeuron){
			activation = SigMoid.sigmoid(activation);
		}
		return activation;
	}

	public void mutate() {
		RandomDeliverer instance = RandomDeliverer.getInstance();
		ArrayList<BigDecimal> newWeights = new ArrayList<BigDecimal>();
		for (int i = 0; i<weights.size(); i++) {
			BigDecimal weight = weights.get(i);
			weight = weight.add(instance.getMutation());
			newWeights.add(weight);
		}
		weights = newWeights;
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public void setInputNeurons(List<Neuron> inputNeurons) {
		this.inputNeurons = inputNeurons;
	}
	
	
	public void addInputNeuron(Neuron inputNeuron){
		RandomDeliverer r = RandomDeliverer.getInstance();
		BigDecimal weight = r.getMutation();
		inputNeurons.add(inputNeuron);
		weights.add(weight);
		if(inputNeurons.size() != weights.size()){
			throw new RuntimeException("Weights und Inputs not Equal!");
		}
		
	}

	public boolean isOutputNeuron() {
		return isOutputNeuron;
	}

	public void setOutputNeuron(boolean isOutputNeuron) {
		this.isOutputNeuron = isOutputNeuron;
	}

}
