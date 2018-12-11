package jhe.lin.main.assignments;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentsUseGA implements jhe.lin.util.GAflow {

	private final int CHROMOSOME_LIMIT;
	private final int GENERATIONS;
	private final float MUTATION_RATE;
	public static HashMap<WorkType.TYPE, WorkType> workTypes = new HashMap<WorkType.TYPE, WorkType>();
	public static HashMap<String, Worker> workers = new HashMap<String, Worker>();
	public static ArrayList<HashMap> requirements = new ArrayList<HashMap>();
	private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();

	public AssignmentsUseGA() {
		CHROMOSOME_LIMIT = 5; // 染色體數量
		GENERATIONS = 1; // 要跑幾個世代
		MUTATION_RATE = 0.05f; // 突變率
	}

	@Override
	public void initParameters() {
		{// 初始化工作類型
			WorkType typeA = new WorkType(1, 2, 4, 6);
			WorkType typeB = new WorkType(0.5f, 1, 2, 4);
			WorkType typeC = new WorkType(2, 3, 5, 10);
			workTypes.put(WorkType.TYPE.A, typeA);
			workTypes.put(WorkType.TYPE.B, typeB);
			workTypes.put(WorkType.TYPE.C, typeC);
		}

		{// 初始化人力資源
			Worker 甲 = new Worker(0.8f, 1, 0.9f);
			Worker 乙 = new Worker(1f, 0.9f, 0.9f);
			Worker 丙 = new Worker(0.9f, 0.9f, 0.95f);
			workers.put("甲", 甲);
			workers.put("乙", 乙);
			workers.put("丙", 丙);
		}

		{// 初始化工作數量
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.A);
				work.put("level", WorkType.LEVEL.EASY);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.A);
				work.put("level", WorkType.LEVEL.NORMAL);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.B);
				work.put("level", WorkType.LEVEL.EASY);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.B);
				work.put("level", WorkType.LEVEL.NORMAL);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.C);
				work.put("level", WorkType.LEVEL.EASY);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.C);
				work.put("level", WorkType.LEVEL.COMPLEX);
				requirements.add(work);
			}
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.C);
				work.put("level", WorkType.LEVEL.VERY_COMPLEX);
				requirements.add(work);
			}
		}
	}

	@Override
	public void initPopulation() {
		for (int i = 0; i < CHROMOSOME_LIMIT; i++) {
			Chromosome chromosome = Chromosome.createByRandom();
			System.err.print(chromosome.toString());
			System.err.println("getFitnessValue : " + chromosome.getFitnessValue());
			System.err.println();
			chromosomes.add(chromosome);
		}
	}

	@Override
	public boolean evalFitness() {
		return false;
	}

	@Override
	public void selection() {

	}

	@Override
	public void crossover() {

	}

	@Override
	public void mutation() {

	}

	@Override
	public void outputResult() {

	}

}
