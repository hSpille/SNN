package de.hspille.neural;

import java.io.Serializable;
import java.math.BigDecimal;

import de.hspille.neural.random.RandomDeliverer;

public class Input implements Serializable {

	private static final long serialVersionUID = 4374353519536560147L;
	private BigDecimal input;
	private BigDecimal weight;

	public Input() {
		RandomDeliverer r = RandomDeliverer.getInstance();
		weight = r.getMutation();
	}

	public void setInput(BigDecimal input) {
		this.input = input;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal calculateOutput() {

		return input;
	}

	public void mutate() {
		RandomDeliverer instance = RandomDeliverer.getInstance();
		weight = weight.add(instance.getMutation());
	}

	public Input copyMe() {
		Input copy = new Input();
		copy.setInput(this.input.add(BigDecimal.ZERO));
		copy.setWeight(this.weight.add(BigDecimal.ZERO));
		return copy;
	}

}
