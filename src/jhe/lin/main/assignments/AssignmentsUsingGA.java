package jhe.lin.main.assignments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jhe.lin.main.GAMain;

@SuppressWarnings("rawtypes")
public class AssignmentsUsingGA implements jhe.lin.util.GAflow<Chromosome> {

	private final int CHROMOSOME_LIMIT;
	private final int GENERATIONS;
	private final int SETECTION_COUNT;
	private final double MUTATION_RATE;
	public static HashMap<WorkType.TYPE, WorkType> workTypes = new HashMap<WorkType.TYPE, WorkType>();
	public static HashMap<String, Worker> workers = new HashMap<String, Worker>();
	public static ArrayList<HashMap> requirements = new ArrayList<HashMap>();
	private ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();
	private ArrayList<Chromosome> selectedChromosomes = new ArrayList<Chromosome>();
	private Chromosome theBestChromosome;
	public static BigDecimal BEST_ANSWER;
	private int generationCount = 1;

	public AssignmentsUsingGA() {
		CHROMOSOME_LIMIT = 10; // 總染色體數量
		SETECTION_COUNT = 6; // 挑選幾組做交配，必須小於總染色體數量
		GENERATIONS = 100000; // 要跑幾個世代
		MUTATION_RATE = 0.001; // 突變率
		BEST_ANSWER = new BigDecimal("10"); // 已知最佳解
		generationCount = 0; // 初始化世代
		
		workTypes.clear();
		workers.clear();
		requirements.clear();
	}

	@Override
	public void initParameters() {
		{// 初始化工作類型 Easy Normal Hard VeryHard
			WorkType typeA = new WorkType("1", "2", "4", "6");
			WorkType typeB = new WorkType("0.5", "1", "2", "4");
			WorkType typeC = new WorkType("1", "3", "5", "10");
			workTypes.put(WorkType.TYPE.A, typeA);
			workTypes.put(WorkType.TYPE.B, typeB);
			workTypes.put(WorkType.TYPE.C, typeC);
		}

		{// 初始化人力資源 A B C
			Worker 甲 = new Worker("0.8", "1", "0.8");
			Worker 乙 = new Worker("1", "0.8", "0.8");
			Worker 丙 = new Worker("0.8", "0.8", "1");
			workers.put("甲", 甲);
			workers.put("乙", 乙);
			workers.put("丙", 丙);
		}

		for (int i = 0; i < 10; i++) {// 初始化工作數量
			{
				HashMap<String, Enum> work = new HashMap<String, Enum>();
				work.put("type", WorkType.TYPE.A);
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
		}
	}

	@Override
	public void initPopulation() {
		for (int i = 0; i < CHROMOSOME_LIMIT; i++) {
			Chromosome chromosome = Chromosome.createByRandom();
			chromosomes.add(chromosome);
		}

		// 排序:從最好到最壞
		Collections.sort(chromosomes);
		if (GAMain.SHOW_LOG) {
			for (Chromosome ch : chromosomes) {
				System.err.print(ch.toString());
				System.err.println();
			}
		}
	}

	@Override
	public boolean evalFitness() {
		if (theBestChromosome != null) {
			Chromosome tempCh = getBestOneOnThisGen();
			if (new BigDecimal(theBestChromosome.getFitnessValue())
					.compareTo(new BigDecimal(tempCh.getFitnessValue())) > 0) {
				theBestChromosome = tempCh;
				if (GAMain.SHOW_LOG) {
					System.err.print("==第");
					System.err.print(generationCount);
					System.err.print("世代== ");
					System.err.println("The Best Fitness Value=" + theBestChromosome.getFitnessValue());
				}
			}
		} else {
			theBestChromosome = getBestOneOnThisGen();
		}

		if (BEST_ANSWER != null) {
			BigDecimal nowBest = new BigDecimal(theBestChromosome.getFitnessValue());
			// 已經得到最佳解就離開
			if (BEST_ANSWER.compareTo(nowBest) == 0) {
				return true;
			}
		}

		// 已經到達設定的世代數就離開
		if (generationCount == GENERATIONS) {
			return true;
		}
		generationCount++;

		return false;
	}

	@Override
	public void selection() {
		selectedChromosomes.clear();

		// 亂數取[SETECTION_COUNT]個染色體等等進行交配
		while (selectedChromosomes.size() < SETECTION_COUNT) {
			int randomIndex = (int) (Math.random() * chromosomes.size());
			if (!selectedChromosomes.contains(chromosomes.get(randomIndex))) {
				selectedChromosomes.add(chromosomes.get(randomIndex));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void crossover() {
		int i = 0;
		while (i < (SETECTION_COUNT / 2)) {
			Chromosome parent1 = selectedChromosomes.get(i);
			Chromosome parent2 = selectedChromosomes.get(i + 1);
			ArrayList<HashMap> parent1List = parent1.toList();
			ArrayList<HashMap> parent2List = parent2.toList();
			ArrayList<HashMap> childList = new ArrayList<HashMap>();
			int point = (int) (Math.random() * (requirements.size() - 2)) + 1; // 1~size-2
			// 取parent1的前半段給child
			for (int index = 0; index < point; index++) {
				childList.add(parent1List.get(index));
			}

			// 以parent2的順序將其餘的值補給child
			for (HashMap<String, Enum> map : parent2List) {
				for (int index = point; index < parent1List.size(); index++) {
					HashMap<String, Enum> parentMap = parent1List.get(index);
					boolean sameType = map.get("type").compareTo(parentMap.get("type")) == 0;
					if (!sameType) {
						continue;
					}
					boolean sameLevel = map.get("level").compareTo(parentMap.get("level")) == 0;
					if (!sameLevel) {
						continue;
					}
					childList.add(parentMap);
					parent1List.remove(index);
				}
			}
			// 將childList做亂數rotate
			point = (int) (Math.random() * (requirements.size() - 2)) + 1;// 1~size-2
			Collections.rotate(childList, point);

			// 亂數排出每個人分配到的工作量
			ArrayList<Integer> spiltArray = new ArrayList<Integer>();
			int index = 0;
			while (index < workers.size() - 1) {
				int spiltPoint = (int) (Math.random() * (requirements.size() - 1)) + 1; // 1~size-1
				if (spiltArray.contains(spiltPoint)) {
					continue;
				}
				spiltArray.add(spiltPoint);
				index++;
			}
			Collections.sort(spiltArray);

			// 用交配出的childList跟每個人分配到的工作量建出新的Chromosome
			int spiltIndex = 0;
			int startIndex = 0;
			int endIndex = 0;
			HashMap<String, List> value = new HashMap<String, List>();
			for (String key : workers.keySet()) {
				ArrayList<HashMap> singlePeopleWorks = new ArrayList<HashMap>();
				if (spiltIndex < spiltArray.size()) {
					endIndex = spiltArray.get(spiltIndex);
				} else {
					endIndex = requirements.size();
				}
				for (int x = startIndex; x < endIndex; x++) {
					singlePeopleWorks.add(childList.get(x));
				}
				value.put(key, singlePeopleWorks);
				startIndex = endIndex;
				spiltIndex++;
			}
			chromosomes.add(new Chromosome(value));
			i++;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mutation() {
		for (Chromosome ch : chromosomes) {
			double random = Math.random();
			if (random < MUTATION_RATE) {
				// 亂數取兩個人，將他們工作直接互換
				HashMap<String, List> value = ch.getValue();
				String key1 = null, key2 = null;
				while (key1 == null || key2 == null || key1.equals(key2)) {
					for (String key : workers.keySet()) {
						if (Math.random() > 0.5) {
							if (key1 == null) {
								key1 = key;
							} else {
								key2 = key;
							}
						}
					}
				}
				ArrayList<HashMap> temp = new ArrayList<HashMap>(value.get(key1));
				value.put(key1, value.get(key2));
				value.put(key2, temp);
				ch.resetValue(value);
			}
		}
	}

	public void sortAndPrepareNextGen() {
		// 排序:從最好到最壞
		Collections.sort(chromosomes);

		// 只取最好的前幾筆進入下一代
		ArrayList<Chromosome> temp = new ArrayList<Chromosome>();
		for (int i = 0; i < CHROMOSOME_LIMIT; i++) {
			temp.add(chromosomes.get(i));
		}
		chromosomes = temp;
	}

	@Override
	public Chromosome outputResult() {
		return theBestChromosome;
	}

	/**
	 * 取得當前適應值最好(最小)的Chromosome
	 * 
	 * @retrun Chromosome
	 */
	private Chromosome getBestOneOnThisGen() {
		Chromosome bestChromosome = null;
		if (chromosomes.size() == 0) {
			return bestChromosome;
		}
		for (Chromosome ch : chromosomes) {
			if (bestChromosome == null) {
				bestChromosome = ch;
			} else {
				BigDecimal tempBest = new BigDecimal(bestChromosome.getFitnessValue());
				if (new BigDecimal(ch.getFitnessValue()).compareTo(tempBest) < 0) {
					bestChromosome = ch;
				}
			}
		}
		return new Chromosome(bestChromosome.getValue());
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void showWorks(ArrayList<HashMap> workList) {
		StringBuilder sb = new StringBuilder();
		for (HashMap<String, Enum> work : workList) {
			sb.append(work.get("type"));
			sb.append("" + Chromosome.getWorkLevelInt(work.get("level")));
			sb.append(" ");
		}
		System.err.println(sb.toString());
	}
	
	public int getGenerationCount(){
		return generationCount;
	}
	
	//public Chromosome
}
