package jhe.lin.main;

import jhe.lin.main.assignments.AssignmentsUsingGA;

public class GAMain {
	public static void main(String... arg) {
		AssignmentsUsingGA ga = new AssignmentsUsingGA();
		ga.initParameters();
		ga.initPopulation();
		while (!ga.evalFitness()) {
			ga.selection();
			ga.crossover();
			ga.mutation();
			ga.sortAndPrepareNextGen();
		}
		ga.outputResult();
	}
}
