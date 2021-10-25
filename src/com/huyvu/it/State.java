package com.huyvu.it;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author huyvu
 * @Since Oct 25, 2021
 */
public class State implements Comparable<State> {
	private State father;
	private List<Stack<Plate>> columns;
	private int g;
	private int h;

	public State() {
	}

	/**
	 * Clone state
	 * 
	 * @param state
	 */
	public State(State state) {

		List<Stack<Plate>> columns = new ArrayList<>();

		for (Stack<Plate> column : state.columns) {

			Stack<Plate> newColumn = new Stack<>();

			for (Plate plate : column) {
				newColumn.push(new Plate(plate));
			}
			columns.add(newColumn);
		}
		this.columns = columns;
		this.g = state.g;
		this.h = state.h;
	}

	public void setG(int g) {
		this.g = g;
	}

	/**
	 * Move plate between columns
	 * 
	 * 
	 * @param present
	 * @param destination
	 * @throws Exception
	 */
	public void movePlate(int present, int destination) throws Exception {

		Stack<Plate> presentColumn = columns.get(present);
		Stack<Plate> destinationColumn = columns.get(destination);

		Plate temp = presentColumn.pop();

		destinationColumn.push(temp);

	}

	public List<Stack<Plate>> getColumns() {
		return columns;
	}

	public void setColumns(List<Stack<Plate>> columns) {
		this.columns = columns;
	}

	public void printWithStatus() {
		print();
		System.out.print("\nState: ");

		System.out.print("g = " + g);
		System.out.print(", h = " + h);

	}

	public void print() {
		System.out.print("\n^^^^^^^^^^^^^^");
		columns.forEach(e -> {
			System.out.print("\n|");
			e.forEach(System.out::print);
		});

	}

	public int getF() {
		return g + h;
	}

	@Override
	public int compareTo(State o) {
		return this.getF() - o.getF();
	}

	@Override
	public int hashCode() {
		return columns.hashCode();
	}

	/**
	 * Calculate H
	 * 
	 * @param goal
	 */
	public void calculateH(State goal) {

		// Must clone because of pointer
		State thisState = new State(this);
		State goalState = new State(goal);

		Stack<Plate> currentColumn = thisState.columns.get(2);
		Stack<Plate> goalColumn = goalState.columns.get(2);

		int n = goalColumn.size(); // number of all plate
		int m = 0; // number of right position plate
		for (int i = 0; i < n; i++) {
			try {
				if (currentColumn.get(i).equals(goalColumn.get(i))) {
					m++;

				}
			} catch (Exception e) {
				break;
			}
		}

		int k = 0; // number of false position plate

		for (int i = 0; i < currentColumn.size(); i++) {
			try {
				if (!currentColumn.get(i).equals(goalColumn.get(i))) {
					k++;
				}
			} catch (Exception e) {
				break;
			}
		}

		this.h = n - m + k;

	}

	/**
	 * This function only use for check the current State is equals the goal state
	 * Only equals the last column
	 * 
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		State state = (State) obj;

		State thisState = new State(this);

		State goalState = new State(state);

		return thisState.columns.equals(goalState.columns);
	}

	public boolean isNotValid() {

		for (int i = 0; i < 3; i++) {
			Stack<Plate> column = columns.get(i);
			if (columnIsNotSorted(column)) {
				return true;

			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean columnIsNotSorted(Stack<Plate> column) {

		Stack<Plate> sorted = (Stack<Plate>) column.clone();
		sorted.sort((e1, e2) -> e1.compareTo(e2));

		boolean result = !column.equals(sorted);

		return result;
	}

	/**
	 * @return the father
	 */
	public State getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(State father) {
		this.father = father;
	}

	/**
	 * Calculate G
	 * 
	 * @param bestState
	 */
	public void calculateG(State bestState) {
		this.g = bestState.g + 1;

	}

}
