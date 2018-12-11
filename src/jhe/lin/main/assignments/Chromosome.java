package jhe.lin.main.assignments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static jhe.lin.main.assignments.AssignmentsUseGA.*;

public class Chromosome implements jhe.lin.util.BaseChromosome<Float> {

	public static Chromosome createByRandom() {
		HashMap<String, List> value = new HashMap<String, List>();
		for (String key : workers.keySet()) {
			value.put(key, new ArrayList<HashMap>());
		}

		Object keys[] = value.keySet().toArray();
		for (HashMap map : requirements) {
			int index = (int) (Math.random() * workers.size()); // 亂數出0 ~
																// workers.size()
																// - 1
			value.get(keys[index].toString()).add(map);
		}
		Chromosome chromosome = new Chromosome(value);
		return chromosome;
	}

	private HashMap<String, List> value;

	public Chromosome(HashMap<String, List> _value) {
		value = _value;
	}

	@Override
	public Float getFitnessValue() {
		HashMap<String, Float> resultMap = new HashMap<String, Float>();
		Float result = 0f;
		for (String key : value.keySet()) {
			ArrayList<HashMap> works = (ArrayList<HashMap>) value.get(key);
			for (HashMap<String, Enum> work : works) {
				// 取得該人力的開發效率
				float efficiency = workers.get(key).getEfficiency().get(work.get("type"));

				// 取得該工作項目的標準開發時間
				float devStandardDays = workTypes.get(work.get("type")).getDevStandardDays().get(work.get("level"));

				// 實際開發時間 = 開發效率 * 標準開發時間
				float realWorkTime = efficiency * devStandardDays;

				Float preValue = resultMap.get(key);
				resultMap.put(key, preValue == null ? realWorkTime : realWorkTime + preValue);
			}
			if (resultMap.get(key) != null && resultMap.get(key) > result) {
				result = resultMap.get(key);
			}
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
			sb.append("] \n");
		}
		return sb.toString();
	}

	private int getWorkLevelInt(Enum e) {
		if (e.compareTo(WorkType.LEVEL.EASY) == 0) {
			return 1;
		} else if (e.compareTo(WorkType.LEVEL.NORMAL) == 0) {
			return 2;
		} else if (e.compareTo(WorkType.LEVEL.COMPLEX) == 0) {
			return 3;
		} else if (e.compareTo(WorkType.LEVEL.VERY_COMPLEX) == 0) {
			return 4;
		}
		return -1;
	}
}
