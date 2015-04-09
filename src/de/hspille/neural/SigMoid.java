package de.hspille.neural;

import java.math.BigDecimal;

public class SigMoid {

	
	public static BigDecimal sigmoid(BigDecimal x)
	{
	    return new BigDecimal (1 / (1 + Math.exp(x.negate().doubleValue())));
	}
}
