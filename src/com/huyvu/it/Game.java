package com.huyvu.it;

import java.util.ArrayList;
import java.util.Collections;
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

	public Set<State> open;
	public Set<State> closed;

	/**
	 * Core method
	 * 
	 * @return
	 */
	public State resolve(int numberOfPlate) {

		final State goal = initGoalState(numberOfPlate);

		open = initOpen(goal);

		closed = new LinkedHashSet<>();

		while (!open.isEmpty()) {

			State bestState = getBestState();

			open.remove(bestState);

			closed.add(bestState);

			if (bestState.equals(goal)) {
				return bestState;
			}

			else {
				List<State> childStates = generateChildStates(bestState);

				for (State child : childStates) {

					child.calculateH(goal);
					child.calculateG(bestState);
					open.add(child);
				}
			}

		}

		return null;

	}

	/**
	 * @param numberOfPlate
	 * @return
	 */
	private State initGoalState(int numberOfPlate) {

		List<Stack<Plate>> finishColumns = new ArrayList<>();

		finishColumns.add(new Stack<>());
		finishColumns.add(new Stack<>());
		finishColumns.add(generatePlateStack(numberOfPlate));

		State goal = new State();
		goal.setColumns(finishColumns);

		goal.setFather(null);

		return goal;
	}

	/**
	 * @param numberOfPlate
	 * @return
	 */
	private Set<State> initOpen(State goal) {

		State rootState = new State(goal);

		List<Stack<Plate>> columns = rootState.getColumns();

		Collections.reverse(columns);

		rootState.calculateH(goal);

		Set<State> open = new LinkedHashSet<>();

		open.add(rootState);

		return open;

	}

	/**
	 * generate all child State by father State and also calculate G & H in child
	 * state compared to father
	 * 
	 * @param fatherState
	 * @return List of State
	 */
	public List<State> generateChildStates(State fatherState) {
		List<State> childStates = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == j)
					continue;

				try {
					State childState = new State(fatherState);
					childState.movePlate(i, j);

					if (closed.contains(childState)) {
						throw new Exception("Is contains in closed!");
					}

					childState.setFather(fatherState);

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

		State result = open.stream().min(Comparator.comparing(e -> e.getF())).get();

		System.out.println("\n================================open");
		open.forEach(State::printWithStatus);
		System.out.println("\n================================best");
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

	Scanner sc = new Scanner(System.in);

	public void run() {

		int numberOfState = 0;
		State result = null;
		do {
			System.out.println("\n***************************");
			System.out.print("Number of plate: ");

			numberOfState = sc.nextInt();

			// start time
			long t0 = System.nanoTime();
			result = resolve(numberOfState);
			// end time
			long t1 = System.nanoTime();

			printResult(result);

			System.out.println("Number of plate: " + numberOfState);
			double elapsedTimeInSecond = (double) (t1 - t0) / 1_000_000_000;
			System.out.println("Elapsed time =" + elapsedTimeInSecond + " seconds");

		} while (numberOfState > 0);

	}

	/**
	 * @param result
	 */
	private void printResult(State result) {
		System.out.print("\n================>Step to goal reverse:");

		int step = 0;
		while (result != null) {
			result.print();
			result = result.getFather();
			step++;
		}
		System.out.println("\n################  move " + (step - 1) + " time");

	}

}
