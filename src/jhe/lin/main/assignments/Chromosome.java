package jhe.lin.main.assignments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jhe.lin.main.assignments.AssignmentsUsingGA.*;

@SuppressWarnings({ "unchecked","rawtypes" })
public class Chromosome implements jhe.lin.util.BaseChromosome<String>, Comparable<Chromosome> {
	private BigDecimal fitnessValue = BigDecimal.ZERO;
	private HashMap<String, BigDecimal> WorkTimeMap;
	private HashMap<String, List> value;
	
	public static Chromosome createByRandom() {
		HashMap<String, List> value = new HashMap<String, List>();
		for (String key : workers.keySet()) {
			value.put(key, new ArrayList<HashMap>());
		}

		Object keys[] = value.keySet().toArray();
		for (HashMap map : requirements) {
			int index = (int) (Math.random() * workers.size()); // 亂數出0~workers.size()-1
			value.get(keys[index].toString()).add(map);
		}
		Chromosome chromosome = new Chromosome(value);
		return chromosome;
	}
	
	public Chromosome(HashMap<String, List> _value) {
		value = _value;
		calculateFitnessValue();
	}
	
	public HashMap<String, List> getValue(){
		return value;
	}
	
	public void resetValue(HashMap<String, List> _value){
		value = _value;
		calculateFitnessValue();
	}
	
	@Override
	public String getFitnessValue() {
		if (fitnessValue.compareTo(BigDecimal.ZERO) != 0) {
			return fitnessValue.toString();
		} else {
			calculateFitnessValue();
		}
		return fitnessValue.toString();
	}

	@Override
	public void calculateFitnessValue() {
		WorkTimeMap = new HashMap<String, BigDecimal>();
		for (String key : value.keySet()) {
			ArrayList<HashMap> works = (ArrayList<HashMap>) value.get(key);
			for (HashMap<String, Enum> work : works) {
				// 取得該人力的開發效率
				BigDecimal efficiency = new BigDecimal(workers.get(key).getEfficiency().get(work.get("type")));

				// 取得該工作項目的標準開發時間
				BigDecimal devStandardDays = new BigDecimal(
						workTypes.get(work.get("type")).getDevStandardDays().get(work.get("level")));

				// 實際開發時間 = 標準開發時間 * 開發效率
				BigDecimal realWorkTime = devStandardDays.divide(efficiency, 3, BigDecimal.ROUND_UP);

				BigDecimal preValue = WorkTimeMap.get(key);
				WorkTimeMap.put(key, preValue == null ? realWorkTime : realWorkTime.add(preValue));
			}

			// 適應值 = Max(每個人實際工作時間)
			if (WorkTimeMap.get(key) != null && WorkTimeMap.get(key).compareTo(fitnessValue) > 0) {
				fitnessValue = WorkTimeMap.get(key);
			}
		}
	}

	public HashMap<String, BigDecimal> getWorkTimeMap() {
		return WorkTimeMap;
	}

	public ArrayList<HashMap> toList() {
		ArrayList<HashMap> result = new ArrayList<HashMap>();
		for (String key : value.keySet()) {
			result.addAll(value.get(key));
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : value.keySet()) {
			sb.append(key);
			sb.append(":");
			ArrayList<HashMap> works = (ArrayList<HashMap>) value.get(key);
			sb.append("[ ");
			for (HashMap<String, Enum> work : works) {
				sb.append(work.get("type"));
				sb.append("" + getWorkLevelInt(work.get("level")));
				sb.append(" ");
			}
			sb.append("] ");
			sb.append("work time = ");
			if (WorkTimeMap.get(key) != null) {
				sb.append(WorkTimeMap.get(key).toString());
			}else{
				sb.append("0.0");
			}
			sb.append("\n");
		}
		sb.append("FitnessValue = " + fitnessValue);
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public int compareTo(Chromosome other) {
		return this.fitnessValue.compareTo(other.fitnessValue);
	}

	public static int getWorkLevelInt(Enum e) {
		if (e.compareTo(WorkType.LEVEL.EASY) == 0) {
			return 1;
		} else if (e.compareTo(WorkType.LEVEL.NORMAL) == 0) {
			return 2;
		} else if (e.compareTo(WorkType.LEVEL.HARD) == 0) {
			return 3;
		} else if (e.compareTo(WorkType.LEVEL.VERY_HARD) == 0) {
			return 4;
		}
		return -1;
	}
}
