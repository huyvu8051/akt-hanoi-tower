package com.huyvu.it;


/**
 * @author huyvu
 * @Since Oct 25, 2021
 */
public class Plate implements Comparable<Plate> {
	private Integer size;
	
	public Plate(int size) {
		this.size = size;
	}
	
	public Plate(Plate plate) {
		this.size = plate.size;
	}
	
	@Override
	public int compareTo(Plate o) {
		
		return o.size - this.size;
	}

	@Override
	public String toString() {
		return Integer.toString(size) + " ";
	}
	
	@Override
	public boolean equals(Object obj) {
		Plate plate = (Plate) obj;
		
		return this.size == plate.size;
	}
	
	@Override
	public int hashCode() {
		return size.hashCode();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
