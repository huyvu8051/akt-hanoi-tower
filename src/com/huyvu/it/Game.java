package com.huyvu.it;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Game {
	
	public List<State> stepToGoal;
	public List<State> open;
	public List<State> closed;
	public State goal;

	public Game() {

	}

	public Game(int numOfPlate) {

		stepToGoal = new ArrayList<>();
		open = new ArrayList<>();
		closed = new ArrayList<>();

		State state = new State();
		goal = new State();
		// generate state and finish

		List<Stack<Plate>> stateColumns = new ArrayList<>();

		stateColumns.add(generatePlateStack(numOfPlate));
		stateColumns.add(new Stack<>());
		stateColumns.add(new Stack<>());

		state.setColumns(stateColumns);

		List<Stack<Plate>> finishColumns = new ArrayList<>();

		finishColumns.add(new Stack<>());
		finishColumns.add(new Stack<>());
		finishColumns.add(generatePlateStack(numOfPlate));

		goal.setColumns(finishColumns);

		open.add(state);

	}

	/**
	 * Core method
	 * 
	 * @return
	 */
	public boolean resolve() {
		State bestState = getBestState();
		
		stepToGoal.add(bestState);

		System.out.println("\n=======bestStatus");
		bestState.print();

		closed.addAll(open);

		System.out.println("\n=======Closed");
		/*
		 * closed.forEach(e -> { e.print(); });
		 */

		open = new ArrayList<>();

		// closed add

		System.out.println("\n=======Open");
		if (bestState.equals(goal)) {
			return true;
		}

		else {
			List<State> childState = generateChildStates(bestState);

			childState.forEach(e -> {
				open.add(e);
				e.printWithStatus();
			});
		}

		return false;

	}

	/**
	 * generate all child State by father State also calculate G & H in child state
	 * compared to father
	 * 
	 * @param state
	 * @return List of State
	 */
	public List<State> generateChildStates(State state) {
		List<State> childStates = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j)
					continue;

				try {
					State childState = new State(state);
					childState.movePlate(i, j);
					childState.calculateState(goal);

					if (closed.contains(childState)) {
						System.out.println("movePlate(" + i + ", " + j + ") contains in closed");
						throw new Exception("Is contains in closed!");
					}

					// is valid move

					if (childState.isNotValid()) {
						System.out.println("movePlate(" + i + ", " + j + ") is not valid");
						throw new Exception("Not valid state!");
					}

					System.out.println("movePlate(" + i + ", " + j + ") successful");
					childStates.add(childState);

				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("movePlate(" + i + ", " + j + ") false");
				}
			}
		}

		return childStates;
	}

	public State getBestState() {
		open.sort((e1, e2) -> e1.compareTo(e2));

		State result = open.get(0);

		return result;
	}

	/**
	 * Generate plate for one column by number of plate
	 * 
	 * @param numOfPlate
	 * @return
	 */
	private Stack<Plate> generatePlateStack(int numOfPlate) {
		Stack<Plate> stack = new Stack<>();
		for (int i = numOfPlate; i > 0; i--) {
			stack.push(new Plate(i));
		}
		return stack;
	}

	public void printOpenWithStatus() {
		this.open.forEach(e -> {
			e.printWithStatus();
		});
	}

	Scanner sc = new Scanner(System.in);

	public void run() {
		int select = 0, count = 1;
		do {
			System.out.println("\n++++++++++++++++++++++++++++++++");
			System.out.println("1.resolve step by step");
			System.out.println("2.resolve all");
			System.out.println("3.print step to goal");
			System.out.println("++++++++++++++++++++++++++++++++");

			select = sc.nextInt();

			switch (select) {
			case 1:
				System.out.println("\n################  solve " + count++ + " time");
				if (resolve()) {
					System.out.println("\nFinishGame");
					return;
				}
				break;
			case 2:
				System.out.println("\n################  solve " + count++ + " time");
				boolean b;
				do {
					b = resolve();
					if (b) {
						System.out.println("\nFinishGame");
					}
				}while(!b);
				break;
			case 3:
				System.out.println("\nStep to goal:");
				stepToGoal.forEach(e->e.print());
				break;

			default:
				break;
			}

		} while (true);

	}

}
