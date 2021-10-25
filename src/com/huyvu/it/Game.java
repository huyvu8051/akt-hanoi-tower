package com.huyvu.it;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * @author huyvu
 * @Since Oct 25, 2021
 */
public class Game {

	public List<State> stepToGoal;
	public List<State> open;
	public Set<State> closed;
	public State goal;

	public Game() {

	}

	public Game(int numOfPlate) {

		stepToGoal = new ArrayList<>();
		open = new ArrayList<>();
		closed = new LinkedHashSet<>();

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
		state.setG(-1);
		state.calculateState(goal);
		open.add(state);

	}

	/**
	 * Core method
	 * 
	 * @return
	 */
	public boolean resolve() {
		while (!open.isEmpty()) {
			State bestState = getBestState();

			stepToGoal.add(bestState);

			closed.addAll(open);

			open = new ArrayList<>();

			if (bestState.equals(goal)) {
				return true;
			}

			else {
				List<State> childStates = generateChildStates(bestState);

				for (State child : childStates) {
					child.calculateState(goal);

					open.add(child);
				}
			}

		}

		return false;

	}

	/**
	 * generate all child State by father State and also calculate G & H in child
	 * state compared to father
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

					if (closed.contains(childState)) {
						throw new Exception("Is contains in closed!");
					}

					if (childState.isNotValid()) {
						throw new Exception("Not valid state!");
					}

					childStates.add(childState);

				} catch (Exception e) {
				}
			}
		}

		return childStates;
	}

	/**
	 * Get the best state in open where F is smallest
	 * 
	 * @return
	 */
	public State getBestState() {

		// print the list of open
		System.out.print("\n\n\n\n\n\n\n\n\n\nOpen: ");
		open.forEach(State::printWithStatus);

		State result = open.stream().min(Comparator.comparing(e -> e.getF())).get();

		// print the best state in open
		System.out.print("\n\n\n=>BestState");
		result.printWithStatus();

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

		int select = 0;
		do {
			System.out.println("\n++++++++++++++++++++++++++++++++");
			System.out.println("1.resolve");
			System.out.println("2.print step to goal");
			System.out.println("++++++++++++++++++++++++++++++++");

			select = sc.nextInt();

			switch (select) {
			case 1:
				resolve();
				System.out.println("\nFinishGame");
				break;
			case 2:
				System.out.println("\nStep to goal:");
				stepToGoal.forEach(e -> e.print());
				System.out.println("\n################  solve " + (stepToGoal.size() - 1) + " time");
				break;

			default:
				break;
			}

		} while (true);

	}

}
