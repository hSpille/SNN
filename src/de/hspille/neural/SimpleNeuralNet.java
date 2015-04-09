package de.hspille.neural;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleNeuralNet implements Serializable {

	private static final long serialVersionUID = 3010412081774507783L;

	private BigDecimal myFitness = new BigDecimal(0);
	private List<Input> inputs = new ArrayList<Input>();
	private Map<Integer, List<Neuron>> hiddenLayer = new HashMap<Integer, List<Neuron>>();
	private List<Neuron> outputLayer = new ArrayList<Neuron>();

	public void buildNet(int numberInputs, int numberOutputs, int numberHidden,
			int hiddenLayerRows) {
		for (int i = 0; i < numberInputs; i++) {
			Input c = new Input();
			this.inputs.add(c);
		}
		for (int i = 0; i < numberOutputs; i++) {
			Neuron c = new Neuron();
			c.setOutputNeuron(true);
			this.outputLayer.add(c);
		}
		for (int r = 0; r < hiddenLayerRows; r++) {
			ArrayList<Neuron> rowList = new ArrayList<Neuron>();
			for (int i = 0; i < numberHidden / hiddenLayerRows; i++) {
				Neuron c = new Neuron();
				rowList.add(c);
			}
			hiddenLayer.put(r, rowList);
		}
		// Connect First hidden Layer to inputs
		for (Input input : inputs) {
			for (Neuron firstrowNeuron : hiddenLayer.get(0)) {
				firstrowNeuron.getInputs().add(input);
			}
		}
		// Connect Hiddens to row -1 except first row (which is connected do
		// inputs)
		for (int i = 1; i < hiddenLayer.size(); i++) {
			List<Neuron> neuronsGettingInput = hiddenLayer.get(i);
			List<Neuron> neuronsActingAsInput = hiddenLayer.get(i - 1);
			for (Neuron neuron : neuronsGettingInput) {
				for (Neuron myInput : neuronsActingAsInput) {
					neuron.addInputNeuron(myInput);
				}
			}
		}

		List<Neuron> lastRowOfHidden = hiddenLayer.get(hiddenLayer.size() - 1);
		for (Neuron neuron : outputLayer) {
			for (Neuron myInput : lastRowOfHidden) {
				neuron.addInputNeuron(myInput);
			}
		}

		//
		// System.out.println("Net created!");
		// System.out.println("Inputs           : " + inputs.size());
		// System.out.println("HiddenRows       : " + hiddenLayer.size());
		// int size = 0;
		// for (List<Neuron> alist : hiddenLayer.values()) {
		// size = size + alist.size();
		// }
		// System.out.println("Hidden Size Total: " + size);
		// System.out.println("Output Neurons   : " + outputLayer.size());
	}

	public BigDecimal doStep(BigDecimal ... list){
		this.feedInput(Arrays.asList(list));
		BigDecimal value = new BigDecimal(0);
		for (Neuron neuron : outputLayer) {
			value = value.add(neuron.calculateOutPut());
		}
		return value;
	}

	private void feedInput(List<BigDecimal> list) {
		if (list.size() != inputs.size()) {
			throw new RuntimeException("Inputvalues size != Inputs size");
		}
		for (int i = 0; i < list.size(); i++) {
			inputs.get(i).setInput(list.get(i));
		}

	}

	public void mutate() {
		for (Input input : inputs) {
			input.mutate();
		}
		for (List<Neuron> neuronList : hiddenLayer.values()) {
			for (Neuron neuron : neuronList) {
				neuron.mutate();
			}
		}
		for (Neuron output : outputLayer) {
			output.mutate();
		}
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public Map<Integer, List<Neuron>> getHiddenLayer() {
		return hiddenLayer;
	}

	public void setHiddenLayer(Map<Integer, List<Neuron>> hiddenLayer) {
		this.hiddenLayer = hiddenLayer;
	}

	public List<Neuron> getOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(List<Neuron> outputLayer) {
		this.outputLayer = outputLayer;
	}

	public BigDecimal getMyFitness() {
		return myFitness;
	}

	public void setMyFitness(BigDecimal myFitness) {
		this.myFitness = myFitness;
	}

}
