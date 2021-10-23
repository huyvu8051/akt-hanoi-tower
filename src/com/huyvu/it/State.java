package com.huyvu.it;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State implements Comparable<State> {
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
		System.out.print("\n\n\nStatus:");
		System.out.print("\n f = " + getF());
		System.out.print(", g = " + g);
		System.out.print(", h = " + h);
		print();
	}

	public void print() {
		System.out.print("\n++++++++++++++++");
		columns.forEach(e -> {
			System.out.print("\n|");
			e.forEach(System.out::print);
		});
		System.out.print("\n++++++++++++++++");

	}

	public int getF() {
		return g + h;
	}

	@Override
	public int compareTo(State o) {
		return o.getF() - this.getF();
	}

	public void calculateState(State goal) {

		this.g ++;

		State thisState = new State(this);

		State goalState = new State(goal);

		Stack<Plate> currentColumn = thisState.columns.get(2);
		Stack<Plate> goalColumn = goalState.columns.get(2);

		int h = 0;

		for (int i = 0; i < goalColumn.size(); i++) {
			try {
				if (currentColumn.get(i).equals(goalColumn.get(i))) {
					h++;
				}
			} catch (Exception e) {
				break;
			}
		}

		this.h = h;

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

		for (int j = 0; j < 3; j++) {
			Stack<Plate> currentColumn = thisState.columns.get(j);
			Stack<Plate> goalColumn = goalState.columns.get(j);
			for (int i = 0; i < 3; i++) {
				Plate currentPlate = null;
				Plate goalPlate = null;

				try {
					currentPlate = currentColumn.get(i);

				} catch (Exception e) {

				}
				try {
					goalPlate = goalColumn.get(i);

				} catch (Exception e) {

				}

				try {

					if (currentPlate == null && goalPlate == null) {
						continue;
					}

					if (!currentPlate.equals(goalPlate)) {
						return false;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}

		return true;
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

	private boolean columnIsNotSorted(Stack<Plate> column) {
	
		Stack<Plate> sorted = (Stack<Plate>) column.clone();
		sorted.sort((e1, e2)->e1.compareTo(e2));
		
		boolean result = !column.equals(sorted);
		
		
		return result;
	}


}
