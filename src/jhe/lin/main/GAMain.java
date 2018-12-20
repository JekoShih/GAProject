package jhe.lin.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import jhe.lin.main.assignments.AssignmentsUsingGA;

public class GAMain {
	public static boolean SHOW_LOG = true;

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
		jhe.lin.main.assignments.Chromosome theBestChromosome = ga.outputResult();
		System.err.print("==========最佳解出現在第");
		System.err.print(ga.getGenerationCount());
		System.err.println("世代==========");
		System.err.println(theBestChromosome.toString());
		
		/** 以下為實驗rotate的效果所做的測試  */
		/*
		SHOW_LOG = false;
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		int totalCount = 100;
		int bestChCount = 0;
		for (int i = 0; i < totalCount; i++) {
			AssignmentsUsingGA ga = new AssignmentsUsingGA();
			ga.initParameters();
			ga.initPopulation();
			while (!ga.evalFitness()) {
				ga.selection();
				ga.crossover();
				ga.mutation();
				ga.sortAndPrepareNextGen();
			}
			jhe.lin.main.assignments.Chromosome theBestChromosome = ga.outputResult();
			System.err.print("==========最佳解出現在第");
			System.err.print(ga.getGenerationCount());
			System.err.println("世代==========");
			System.err.println(theBestChromosome.toString());
			String fitnessValue = theBestChromosome.getFitnessValue();
			Integer count = resultMap.get(fitnessValue);
			if (count == null) {
				resultMap.put(fitnessValue, 1);
			} else {
				resultMap.put(fitnessValue, count + 1);
			}

			if (new BigDecimal(fitnessValue).compareTo(AssignmentsUsingGA.BEST_ANSWER) == 0) {
				bestChCount++;
			}
		}
		System.err.println(totalCount + "次中得到[" + bestChCount + "]次最佳解!");
		for (String key : resultMap.keySet()) {
			System.err.println("[" + key + "]出現" + resultMap.get(key) + "次");
		}*/
	}

}
