package org.smof.examples;

import java.util.Enumeration;


import org.smof.annnotations.SmofBuilder;
import org.smof.annnotations.*;
import org.smof.element.AbstractElement;
import org.smof.index.IndexType;


@SmofPartialIndexes(fields={@SmofPartialIndex(
		fields ={ @SmofIndexField(name = "capacity", type = IndexType.ASCENDING) }, 
		pfe = @SmofPFEQuery(expression = { @SmofQueryA(query = { @SmofFilter(operator = OperatorType.greaterThan, value = "5") }) }, 
		name = "capacity"))
})
class Bottle extends AbstractElement {
	
	private static final String CAPACITY = "capacity";
	private static final String AMOUNT = "liquid_amount";
	private static final String LIQUID = "liquid";

	@SmofString(name = LIQUID)
	private String liquid;
	
	@SmofNumber(name = AMOUNT)
	private double amount;
	
	@SmofNumber(name = CAPACITY)
	private double capacity;
	
	/**
	 * Returns a new empty bottle
	 * 
	 * @param liquid liquid type
	 * @param capacity total capacity
	 */
	public Bottle(String liquid, double capacity) {
		this(liquid, capacity, 0.0);
	}
	
	/**
	 * General constructor
	 * 
	 * @param liquid liquid type
	 * @param capacity total capacity
	 * @param amount liquid amount
	 */
	@SmofBuilder
	public Bottle(@SmofParam(name=LIQUID) String liquid, 
			@SmofParam(name = CAPACITY) Double capacity, 
			@SmofParam(name = AMOUNT) Double amount) {
		this.liquid = liquid;
		this.capacity = capacity;
		this.amount = amount;
	}
	
	public boolean isFull() {
		return capacity == amount;
	}
	
	public double fill(Double amount) {
		final double left = capacity-amount; 
		if(left < amount) {
			this.amount = capacity;
			return amount-left;
		}
		this.amount += amount;
		return left-amount;
	}

	
}
