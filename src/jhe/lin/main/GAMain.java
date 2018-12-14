package jhe.lin.main;

import jhe.lin.main.assignments.AssignmentsUseGA;

public class GAMain {
	public static final boolean ENABLE_LOGCAT = true;

	public static void main(String... arg) {
		AssignmentsUseGA ga = new AssignmentsUseGA();
		ga.initParameters();
		System.err.println("====================initPopulation start==========================");
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
