/*******************************************************************************
 * Copyright (C) 2017 Joao Sousa
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
package org.smof.dataModel;

import java.nio.file.Path;

import org.smof.annnotations.SmofNumber;
import org.smof.annnotations.SmofObject;
import org.smof.annnotations.SmofString;
import org.smof.element.AbstractElement;
import org.smof.gridfs.SmofGridRef;
import org.smof.gridfs.SmofGridRefFactory;

@SuppressWarnings("javadoc")
public abstract class AbstractGuitar extends AbstractElement implements Guitar{
	
	@SmofObject(name=MODEL)
	private final Model model;
	
	@SmofString(name=TYPE)
	private final TypeGuitar type;
	
	@SmofString(name=OWNER)
	private String owner;
	
	@SmofString(name=COLOR)
	private String color;
	
	@SmofNumber(name=PRICE)
	private int price;
	
	@SmofObject(name = PICTURE, bucketName = StaticDB.GUITARS_PIC_BUCKET, preInsert=false)
	private SmofGridRef picture;
	
	protected AbstractGuitar(Model model, TypeGuitar type) {
		super();
		this.model = model;
		this.type = type;
		price = model.getFactoryPrice();
		picture = SmofGridRefFactory.newEmptyRef();
	}

	@Override
	public Brand getBrand() {
		return model.getBrand();
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public TypeGuitar getGuitarType() {
		return type;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public String getColor() {
		return color;
	}
	
	@Override
	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractGuitar other = (AbstractGuitar) obj;
		if (color == null) {
			if (other.color != null) {
				return false;
			}
		} else if (!color.equals(other.color)) {
			return false;
		}
		if (model == null) {
			if (other.model != null) {
				return false;
			}
		} else if (!model.equals(other.model)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return type.toString() + "-" + model;
	}

	@Override
	public SmofGridRef getPicture() {
		return picture;
	}

	@Override
	public void setPicture(Path picture) {
		this.picture.attachFile(picture);
	}

}
