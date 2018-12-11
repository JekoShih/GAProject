package jhe.lin.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import jhe.lin.util.IO;

public class GAflowImpl implements jhe.lin.util.GAflow {

	private static final int CHROMOSOME_LIMIT = 5; // 染色體數量
	private static final int GENERATIONS = 1; // 要跑幾個世代
	private static final float MUTATION_RATE = 0.05f; // 突變率
	private static final int N = 5; // 景點總數
	private static final int M = 5; // 交通方式總數
	private static final float STAY_TIME = 0.5f; // 每個景點停留時間:0.5小時
	private static final float TIME_LIMIT = 4f; // 限制時間內需走完景點:5小時

	private ArrayList<Map> transportation_modes; // 交通方式
	private ArrayList<Chromosome> chromosomes; // 所有染色體
	private float d[][] = new float[N][N]; // 所有景點到景點的所有距離
	private int currentGeneration = 0;

	@Override
	public void initParameters() {
		System.out.println("--------initParameters--------");
		System.out.println("  染色體數量 = " + CHROMOSOME_LIMIT);
		System.out.println("  世代數量 = " + GENERATIONS);
		System.out.println("  突變率 = " + MUTATION_RATE);
		System.out.println("  景點總數 = " + N);
		System.out.println("  景點停留時間(hr) = " + STAY_TIME);
		System.out.println("  限制時間(hr) = " + TIME_LIMIT);
		System.out.println("  交通方式總數 = " + M);
		d = IO.readExcel_distance(N);
		currentGeneration = 0;
		chromosomes = new ArrayList<Chromosome>();
		transportation_modes = new ArrayList<Map>();
		{
			Map map = new HashMap<String, Object>();
			map.put("name", "走路");
			map.put("avg_speed", 4);
			map.put("CO2emission", 0.012f);
			transportation_modes.add(map);
		}
		{
			Map map = new HashMap<String, Object>();
			map.put("name", "單車");
			map.put("avg_speed", 15);
			map.put("CO2emission", 0.003f);
			transportation_modes.add(map);
		}
		{
			Map map = new HashMap<String, Object>();
			map.put("name", "機車");
			map.put("avg_speed", 27);
			map.put("CO2emission", 0.03f);
			transportation_modes.add(map);
		}
		{
			Map map = new HashMap<String, Object>();
			map.put("name", "汽車");
			map.put("avg_speed", 35);
			map.put("CO2emission", 0.07f);
			transportation_modes.add(map);
		}
		{
			Map map = new HashMap<String, Object>();
			map.put("name", "巴士");
			map.put("avg_speed", 28);
			map.put("CO2emission", 0.05f);
			transportation_modes.add(map);
		}

		if (GAMain.ENABLE_LOGCAT) {
			for (int i = 0; i < transportation_modes.size(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append("  TpModeName = ");
				sb.append(transportation_modes.get(i).get("name").toString());
				sb.append("  TpModeSpeed = ");
				sb.append(Float.parseFloat(transportation_modes.get(i)
						.get("avg_speed").toString()));
				sb.append("km/hr");
				sb.append("  TpModeCO2 = ");
				sb.append(Float.parseFloat(transportation_modes.get(i)
						.get("CO2emission").toString()));
				sb.append("kg/km");
				System.out.println(sb.toString());
			}
		}
	}

	@Override
	public void initPopulation() {
		System.out.println("--------initPopulation--------");
		for (int i = 0; i < CHROMOSOME_LIMIT; i++) {
			Chromosome chromosome = new Chromosome();
			Integer[] spots = new Integer[N];
			for (int x = 0; x < N; x++) {// 初始化陣列中的值 1~25
				spots[x] = x + 1;
			}

			int noVisitSpotCount = (int) (Math.random() * 0); // 亂數出0 ~ ?個不走的點
			for (int j = 0; j < noVisitSpotCount; j++) {
				spots[spots.length - 1 - j] = -1;
			}
			spots = shuffleArray(spots);
			chromosome.setSpot(spots);

			Integer[] modes = new Integer[spots.length - noVisitSpotCount]; // 有多少要去的景點
			// 就有多少交通方式
			for (int k = 0; k < modes.length; k++) {
				modes[k] = (int) (Math.random() * 5); // 亂數出0 ~ 4決定交通方式
			}
			System.out.println("=============Chromosome[" + i
					+ "]================");
			chromosome.setMode(modes);
			chromosome.calFitnessVal();
			chromosome.calSpendTime();
			chromosome.logcat();
			System.out.println();
			chromosomes.add(chromosome);
		}
	}

	@Override
	public boolean evalFitness() {
		System.out.println("--------evalFitness--------");
		System.out
				.println("  CurrentGeneration is [" + currentGeneration + "]");
		if (currentGeneration < GENERATIONS) {
			currentGeneration++;
			return true;
		}
		return false;
	}

	@Override
	public void selection() {
		System.out.println("--------selection--------");
		System.out.println("???黑人問號???");
	}

	@Override
	public void crossover() {
		System.out.println("--------crossover--------");
		for (int i = 0; i < chromosomes.size(); i++) {
			singlePointCrossover(chromosomes.get(i), chromosomes.get(i + 1));
			// chromosomes.set(i, singlePointCrossover(chromosomes.get(i)));
		}
	}

	private Chromosome singlePointCrossover(Chromosome parent1,
			Chromosome parent2) {
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();
		int spotLength = parent1.getSpot().length;
		int point1 = (int) (Math.random() * (spotLength - 2)) + 1; // 亂數切一段
		System.out.println("point1 = " + point1);
		Integer newSpot[] = new Integer[spotLength];
		Integer spot1[] = Arrays.copyOfRange(parent1.getSpot(), 0, point1);
		Integer spot2[] = Arrays.copyOfRange(parent2.getSpot(), point1,
				spotLength);
		int index = 0;
		while (index < spotLength) {
			if (index < spot2.length) {
				newSpot[index] = spot2[index];
			} else {
				newSpot[index] = spot1[index - spot2.length];
			}
			index++;
		}
		child1.setSpot(newSpot);

		spot1 = Arrays.copyOfRange(parent2.getSpot(), 0, point1);
		spot2 = Arrays.copyOfRange(parent1.getSpot(), point1, spotLength);
		index = 0;
		while (index < spotLength) {
			if (index < spot2.length) {
				while (Arrays.asList(newSpot).contains(spot2[index])) {
					spot2[index] = (int) (Math.random() * spotLength) + 1;
					System.out.println("spot2[index] = " + spot2[index]);
				}
				newSpot[index] = spot2[index];
			} else {
				while (Arrays.asList(newSpot).contains(
						spot1[index - spot2.length])) {
					spot1[index - spot2.length] = (int) (Math.random() * spotLength) + 1;
					System.out.println("spot1[index - spot2.length] = "
							+ spot1[index - spot2.length]);
				}
				newSpot[index] = spot1[index - spot2.length];
			}

			index++;
		}
		child2.setSpot(newSpot);

		int modeLength = parent1.getMode().length;
		int point2 = (int) (Math.random() * (modeLength - 2)) + 1; // 亂數切一段
		Integer newMode[] = new Integer[modeLength];
		Integer mode1[] = Arrays.copyOfRange(parent1.getMode(), 0, point2);
		Integer mode2[] = Arrays.copyOfRange(parent2.getMode(), point2,
				modeLength);
		index = 0;
		while (index < modeLength) {
			if (index < mode2.length) {
				newMode[index] = mode2[index];
			} else {
				newMode[index] = mode1[index - mode2.length];
			}
			index++;
		}
		child1.setMode(newMode);
		child1.logcat();

		newMode = new Integer[modeLength];
		mode1 = Arrays.copyOfRange(parent2.getMode(), 0, point2);
		mode2 = Arrays.copyOfRange(parent1.getMode(), point2, modeLength);
		index = 0;
		while (index < modeLength) {
			if (index < mode2.length) {
				newMode[index] = mode2[index];
			} else {
				newMode[index] = mode1[index - mode2.length];
			}
			index++;
		}
		child2.setMode(newMode);
		child2.logcat();
		// child.calFitnessVal();
		// child.calSpendTime();
		// if (child.getSpendTime() > TIME_LIMIT) {
		// child = singlePointCrossover(child);
		// } else {
		// child.logcat();
		// }
		return child1;
	}

	@Override
	public void mutation() {
		System.out.println("mutation");

	}

	@Override
	public void outputResult() {
		System.out.println("output");

	}

	class Chromosome {
		private Integer spot[];
		private Integer mode[];
		private float spendTime; // hr
		private int r;// 此次要去景點的個數
		private float fitnessValue;

		public Integer[] getSpot() {
			return spot;
		}

		public void setSpot(Integer[] spot) {
			this.spot = spot;
		}

		public Integer[] getMode() {
			return mode;
		}

		public void setMode(Integer[] mode) {
			this.mode = mode;
			r = mode.length;
		}

		public float getSpendTime() {
			return spendTime;
		}

		public void setSpendTime(float spendTime) {
			this.spendTime = spendTime;
		}

		public float getFitnessValue() {
			return fitnessValue;
		}

		public float calFitnessVal() {
			this.fitnessValue = 0;
			List<Integer> spotList = Arrays.asList(spot);
			for (int p = 0; p < r; p++) {
				int i, j;
				if (p < mode.length - 1) {
					i = spotList.indexOf(p + 1);
					j = spotList.indexOf(p + 2);
				} else {
					i = spotList.indexOf(mode.length);
					j = spotList.indexOf(1);
				}
				// CO2總排放量(kg) = 加總 排定路線的距離(km)*CO2的單位排放量(kg/km)
				fitnessValue += (d[i][j] * Float
						.parseFloat(transportation_modes.get(mode[p])
								.get("CO2emission").toString()));
			}
			return fitnessValue;
		}

		public float calSpendTime() {
			this.spendTime = 0;
			spendTime = STAY_TIME * r;
			List<Integer> spotList = Arrays.asList(spot);
			for (int p = 0; p < r; p++) {
				int i, j;
				if (p < mode.length - 1) {
					i = spotList.indexOf(p + 1);
					j = spotList.indexOf(p + 2);
				} else {
					i = spotList.indexOf(mode.length);
					j = spotList.indexOf(1);
				}

				// 交通花費時間 = 距離(km)/平均速率(km/hr) + 加總 景點停留時間
				spendTime += (d[i][j] / Float.parseFloat(transportation_modes
						.get(mode[p]).get("avg_speed").toString()));
			}
			return spendTime;
		}

		public void logcat() {
			if (spot != null) {
				System.out.print("[");
				for (int i = 0; i < spot.length; i++) {
					System.out.print(spot[i]);
					if (i < spot.length - 1)
						System.out.print(",");
				}
				System.out.print("]");
			}
			if (mode != null) {
				System.out.print("[");
				for (int i = 0; i < mode.length; i++) {
					System.out.print(mode[i]);
					if (i < mode.length - 1)
						System.out.print(",");
				}
				System.out.print("]");

			}
			System.out.println();
			List<Integer> spotList = Arrays.asList(spot);
			if (GAMain.ENABLE_LOGCAT) {
				for (int p = 0; p < r; p++) {
					int i, j;
					if (p < mode.length - 1) {
						i = spotList.indexOf(p + 1);
						j = spotList.indexOf(p + 2);
					} else {
						i = spotList.indexOf(mode.length);
						j = spotList.indexOf(1);
					}
					StringBuilder sb = new StringBuilder();
					sb.append("d[" + i + "][" + j + "] = ");
					sb.append(d[i][j]);
					sb.append("km");
					sb.append("  TpModeName = ");
					sb.append(transportation_modes.get(mode[p]).get("name")
							.toString());
					sb.append("  交通耗費時間 = "
							+ d[i][j]
							/ Float.parseFloat(transportation_modes
									.get(mode[p]).get("avg_speed").toString()));
					sb.append("hr");
					sb.append("  CO2排放量 = "
							+ d[i][j]
							* Float.parseFloat(transportation_modes
									.get(mode[p]).get("CO2emission").toString()));
					sb.append("kg");
					System.out.println(sb.toString());
				}
			}
			System.out.print("fitnessVal = " + fitnessValue + " (kg)");
			System.out.println();
			System.out.print("spendTime = " + spendTime + " (hr)");
			System.out.println();
		}
	}

	private Integer[] shuffleArray(Integer[] ar) {
		// If running on Java 6 or older, use `new Random()` on RHS here
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
		return ar;
	}
}
