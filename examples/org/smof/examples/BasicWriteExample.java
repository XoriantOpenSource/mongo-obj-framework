/*******************************************************************************
 * Copyright (C) 2017 Joao
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.smof.examples;

import org.smof.annnotations.SmofPartialIndex;
import org.smof.annnotations.SmofPartialIndexes;
import org.smof.collection.Smof;
import org.smof.index.PartialIndex;


@SuppressWarnings("javadoc")
public final class BasicWriteExample {

	public static void main(String[] args) {
		//create the smof object with host, port and database name
		final Smof smof = Smof.create("localhost", 27020, "myDB");
		//create a new bottle
		final Bottle bottle = new Bottle("water", 1.0);
		//create a collection and map it to a type
		smof.createCollection("bottles", Bottle.class);
		//saves the bottle
		smof.insert(bottle);
		//fill the bottle
		bottle.fill(0.5);
		//update the object on the database
		smof.update(Bottle.class).fromElement(bottle);
		
		smof.close();
		/*
		PartialIndex pi=new PartialIndex();
		final Bottle bottle = new Bottle("water", 1.0);
		SmofPartialIndex[] spf=Bottle.class.getAnnotation(SmofPartialIndexes.class).fields();
		
		PartialIndex.createOptions(spf[0]);
		*/
	}

}
