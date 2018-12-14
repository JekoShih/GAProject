package jhe.lin.main.assignments;

import java.util.HashMap;

public class WorkType {
	public enum TYPE {
		A, B, C
	}

	public enum LEVEL {
		EASY, NORMAL, HARD, VERY_HARD
	}

	private HashMap<WorkType.LEVEL, String> devStandardDays = new HashMap<WorkType.LEVEL, String>();
	
	public WorkType(String easy, String normal, String hard, String veryHard) {
		devStandardDays.put(WorkType.LEVEL.EASY, easy);
		devStandardDays.put(WorkType.LEVEL.NORMAL, normal);
		devStandardDays.put(WorkType.LEVEL.HARD, hard);
		devStandardDays.put(WorkType.LEVEL.VERY_HARD, veryHard);
	}

	public HashMap<WorkType.LEVEL, String> getDevStandardDays() {
		return devStandardDays;
	}
}
